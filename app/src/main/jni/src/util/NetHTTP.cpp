//#include "PlatformPrecomp.h"

#include <cstdio>
#include <string>
#include <cstring>
#include <vector>
#include <cmath>
#include <deque>
#include <cassert>
#include <map>
#include <deque>
#include <stdlib.h>
#include <iostream>
#include <sstream>
#include <cstdio>
#include "textscanner.h"
#ifdef __WINPHONE__
	#include <SDL.h>
#else
    #ifdef __ANDROID__
        #include "SDL.h"
        #include <android/log.h>
        #include "androidWrapper/MasterKiwiWrapper.h"
#elif __APPLE__
#include "SDL.h"
    #else
        #include "SDL2/SDL.h"
    #endif
#endif

#include "NetHTTP.h"
#include "NetUtils.h"
#include "miscutils.h"
//#include "BaseApp.h"
//#include "util/TextScanner.h"

#define NET_END_MARK_CHECK_DELAY_MS 333
#define C_END_DOWNLOAD_MARKER_STRING "RTENDMARKERBS1001"
#define C_DEFAULT_IDLE_TIMEOUT_MS (60*1000)

NetHTTP::NetHTTP()
{
	m_pFile = NULL;
	Reset(true);
}

NetHTTP::~NetHTTP()
{
	if (m_pFile)
	{
		fclose(m_pFile);
		//RemoveFile(m_fileName);
		m_pFile = NULL;
	}
}

void NetHTTP::Reset(bool bClearPostdata)
{

	if (m_pFile)
	{
		fclose(m_pFile);
		//RemoveFile(m_fileName);
		m_pFile = NULL;
	}

	m_state = STATE_IDLE;
	m_error = ERROR_NONE;
	m_timer = 0;
	m_idleTimeOutMS = C_DEFAULT_IDLE_TIMEOUT_MS;
	m_expectedFileBytes= 0;
	m_downloadData.clear();
	m_replyHeader.clear();
	m_query.clear();
	if (bClearPostdata)
	{
		m_postData.clear();
	}
	m_bytesWrittenToFile = 0;
}

void NetHTTP::Setup( string serverName, int port, string query )
{
	m_serverName = serverName;
	m_port = port;
	m_query = query;
}

bool NetHTTP::SetFileOutput(const string &fName)
{
	assert(!m_pFile);

    m_fileName = fName; //save for later so we can delete this file if anything goes wrong
    #ifdef __ANDROID__
		#ifdef NEWENGINE_MASTERKIWIWRAPPER_H
			std::string path(getPreferencedPath());
			m_fileName = path + "/" + m_fileName;
		#else
			m_fileName = SDL_GetPrefPath("","") + m_fileName;
		#endif
        SDL_Log("SetFileName: %s",m_fileName.c_str());
    #elif __APPLE__
        m_fileName = SDL_GetPrefPath("","") + m_fileName;
    #endif

	m_pFile = fopen(m_fileName.c_str(), "wb");

	if (!m_pFile)
	{
		OnError(ERROR_WRITING_FILE);
        SDL_Log("ERROR_WRITINGFILE: %s , %s",fName.c_str(),strerror(errno));
		return false;
	}

	return true;
}

bool NetHTTP::AddPostData( const string &name, const byte *pData, int len/*=-1*/ )
{
	if (m_postData.length() != 0)
	{
		//adding to other named post data, so we need a separator
		m_postData += "&";
	}

	//at this stage we need to encode it for safe html transfer, before we get the length
	URLEncoder encoder;

	encoder.encodeData((const byte*)name.c_str(), name.length(), m_postData);
	m_postData += '=';

	if (len == -1) len = strlen((const char*) pData);

	encoder.encodeData(pData, len, m_postData);
#ifdef _DEBUG

	/*
	LogMsg("Postdata is now %s", m_postData.c_str());


	URLDecoder decoder;

	string encoded;
	encoder.encodeData(pData, len, encoded);
	vector<byte> decoded = decoder.decodeData(encoded);
	int sizeOfDecoded = decoded.size();
	LogMsg("Size of decoded should be %u", decoded.size());
	assert(memcmp(pData, &decoded[0], len) == 0 && "Encryption of string error, they don't match");
	*/

#endif
	return true;
}


string NetHTTP::BuildHTTPHeader()
{
	string header, stCommand;

	if (m_postData.length() > 0)
	{
		stCommand = "POST";
	} else
	{
		stCommand = "GET";
	}

	string queryEncoded;

	//URLEncoder encoder;
	//encoder.encodeData( (const byte*)m_query.c_str(), m_query.size(), queryEncoded);

	queryEncoded = m_query;
	StringReplace(" ", "+", queryEncoded);

	header = stCommand + " /"+queryEncoded+" HTTP/1.0\n";
	header += "Accept: */*\n";
	header += "Host: "+m_serverName+"\n";

	if (m_postData.length() > 0)
	{
		header += "Content-Type: application/x-www-form-urlencoded\n";
		header += "Content-Length: "+NumberToString(m_postData.length())+"\n";
	}

	header +="\n"; //add the final CR to indicate we're done with the header

	return header;
}

bool NetHTTP::Start()
{
	m_bytesWrittenToFile = 0;
	m_error = ERROR_NONE;
	m_downloadData.clear();
	m_downloadHeader.clear();
	m_expectedFileBytes = 0;
	string header = BuildHTTPHeader();

#ifdef _DEBUG
	//LogMsg(header.c_str());
#endif
	//take on the post data if applicable
	if (!m_netSocket.Init(m_serverName, m_port))
	{
		OnError(ERROR_CANT_RESOLVE_URL);
		return false;
	}

	m_state = STATE_ACTIVE;
	m_netSocket.Write(header);
	m_netSocket.Write(m_postData);
	return true;
}

const char* NetHTTP::GetStateToPrint()
{
	const char* result = "";
	switch(m_state)
	{
		case(STATE_IDLE):
			result = "STATE_IDLE";
			break;
		case(STATE_ACTIVE):
			result = "STATE_ACTIVE";
			break;
		case(STATE_FINISHED):
			result = "STATE_FINISHED";
			break;
		case(STATE_FORWARD):
			result = "STATE_FORWARD";
			break;
		case(STATE_ABORT):
			result = "STATE_ABORT";
			break;
		default:
			result = "default";
			break;
	}
	return result;
}

const char* NetHTTP::GetErrorToPrint()
{
	const char* result = "";
	switch(m_error)
	{
		case(ERROR_NONE):
			result = "ERROR_NONE";
			break;
		case(ERROR_CANT_RESOLVE_URL):
			result = "ERROR_CANT_RESOLVE_URL";
			break;
		case(ERROR_COMMUNICATION_TIMEOUT):
			result = "ERROR_COMMUNICATION_TIMEOUT";
			break;
		case(ERROR_WRITING_FILE):
			result = "ERROR_WRITING_FILE";
			break;
		case(ERROR_404_FILE_NOT_FOUND):
			result = "ERROR_404_FILE_NOT_FOUND";
			break;
		default:
			result = "";
			break;
	}
	return result;
}


bool CheckCharVectorForString(vector<char> &v, string marker, int *pIndexOfMarkerEndPosOut)
{
	int correctCount = 0;
	assert(marker.size() > 0);

	for (unsigned int i=0; i < v.size(); i++)
	{
		if (v[i] == marker[correctCount] )
		{
			//so far so good
			correctCount++;
			if (correctCount == marker.size())
			{
				if (pIndexOfMarkerEndPosOut)
				{
					*pIndexOfMarkerEndPosOut = i+1;
				}
				return true; //found it
			}
		} else
		{
			//nah
			correctCount = 0;
		}
	}

	return false;
}

int NetHTTP::ScanDownloadedHeader()
{
	TextScanner t;
	t.SetupFromMemoryAddress(m_downloadHeader.c_str());
	string temp = t.GetParmString("Content-Length", 1, ":");
	//std::string temp ="404";
	m_expectedFileBytes = atoi(temp.c_str());

	int resultCode = atol(SeparateStringSTL(t.lineList[0], 1, ' ').c_str());
	//int resultCode = 404;
	switch (resultCode)
	{
		case 404:
			OnError(ERROR_404_FILE_NOT_FOUND);
			break;

		case 301: //moved permanently
		case 302: //moved temporarily

		string url = t.GetParmString("Location:",1, " ");

		if (!url.empty())
		{
			string domain;
			string request;
			int port = 80;
			BreakDownURLIntoPieces(url, domain, request, port);
			string fNameTemp = m_fileName;
			Reset(false); //end connection, setup new one
			if (!fNameTemp.empty())
			{
				SetFileOutput(fNameTemp);
			}
			Setup(domain, port, request);
			Start();
		}

	}

	return resultCode;

}

void NetHTTP::Update()
{
	m_netSocket.Update();

	if (m_state == STATE_ACTIVE)
	{
		//we don't know how many bytes are coming in this case, so we'll look for a special marker.

		if (m_timer < SDL_GetTicks())
		{
			m_timer = SDL_GetTicks()+NET_END_MARK_CHECK_DELAY_MS;

			vector<char> &s = m_netSocket.GetBuffer();

			if (m_downloadHeader.empty())
			{
				//keep trying to locate the header portion
				int indexOfEndOfHeader;
				if (!CheckCharVectorForString(m_netSocket.GetBuffer(), "\r\n\r\n", &indexOfEndOfHeader)
					&& !CheckCharVectorForString(m_netSocket.GetBuffer(), "\n\n", &indexOfEndOfHeader))
				{
					//haven't downloaded enough yet I guess
				} else
				{
					m_downloadHeader.insert(m_downloadHeader.begin(), s.begin(), s.begin()+indexOfEndOfHeader);

					//now would be a good time to scan it...
					int result = ScanDownloadedHeader();
					if (result == 301 || result == 302)
					{
						//new connection was started, let it happen naturally..
						return;
					}
					if (GetState() == STATE_ABORT) return;


					if (m_pFile)
					{
						//we want to start putting things into file, so we'll need to cut this header crap out
						s.erase(s.begin(), s.begin()+indexOfEndOfHeader);

					}
				}

			}

			if (m_expectedFileBytes == 0)
			{
				bool bFoundMarker = CheckCharVectorForString(m_netSocket.GetBuffer(), C_END_DOWNLOAD_MARKER_STRING);
				if (bFoundMarker)
				{
						FinishDownload();
						return;
				}
			} else
			{
				if (m_pFile)
				{
					//we're writing to file instead of the memory method
					if (s.size() > 0)
					{
						//write this to file
						int bytesWritten = fwrite(&s[0], 1, s.size(), m_pFile);
						if (bytesWritten != s.size())
						{
							OnError(ERROR_WRITING_FILE);
						}
						m_bytesWrittenToFile += bytesWritten;
						s.clear();
					}

					if (m_bytesWrittenToFile >= int(m_expectedFileBytes))
					{
						FinishDownload();
						return;
					}
				} else
				{
					//standard way of detecting the end
					if (m_netSocket.GetBuffer().size()-m_downloadHeader.size() >= m_expectedFileBytes)
					{
						FinishDownload();
						return;
					}
				}

			}
		}

		if (m_netSocket.GetIdleTimeMS() > m_idleTimeOutMS)
		{
			OnError(ERROR_COMMUNICATION_TIMEOUT);
			return;
		}
	}
}

void NetHTTP::OnError(eError e)
{
	m_error = e;
	m_state = STATE_ABORT;
	m_netSocket.Kill();

}
void NetHTTP::FinishDownload()
{
	if (m_downloadHeader.empty())
	{
		OnError(ERROR_CANT_RESOLVE_URL);
		return;
	}

	if (m_pFile)
    {
		fclose(m_pFile);
		m_pFile = NULL;
		m_state = STATE_FINISHED;
		return;

	}
	//prepare the data so we can share it later when asked
	vector<char> &s = m_netSocket.GetBuffer();

	if (m_expectedFileBytes == 0)
	{
		m_downloadData.insert(m_downloadData.begin(), s.begin()+m_downloadHeader.length(), s.end()-strlen(C_END_DOWNLOAD_MARKER_STRING));
	} else
	{
		m_downloadData.insert(m_downloadData.begin(), s.begin()+m_downloadHeader.length(), s.end());
	}

	m_downloadData.push_back(0); //useful if used like a string
	m_state = STATE_FINISHED;
}

const byte * NetHTTP::GetDownloadedData()
{
	if (m_downloadData.empty())
	{
		return NULL;
	}
	return (const byte*)&m_downloadData[0];
}

int NetHTTP::GetDownloadedBytes()
{
	if (m_pFile || m_bytesWrittenToFile != 0)
	{
        return m_bytesWrittenToFile;
	}

	if (m_downloadData.size() == 0) return 0;
	return m_downloadData.size()-1; //the -1 is for the null we added
}
