
#ifndef EMSCRIPTEN
#ifdef WINAPI
#ifdef __WINPHONE__
#pragma comment(lib,"ws2_32.lib")
#endif
    #include <winsock2.h>
    #include <windows.h>
    #include <TCHAR.h>
#endif
#include <cstdio>
#include <string>
#include <vector>
#include <cmath>
#include <deque>
#include <cassert>
#include <map>
#include <deque>
#include <stdlib.h>
#include <iostream>
#include <sstream>
#ifdef __WINPHONE__
	#include <SDL.h>
#else
    #ifdef __ANDROID__
        #include "SDL.h"
#elif __APPLE__
#include "SDL.h"
    #else
        #include "SDL2/SDL.h"
    #endif
#endif
using namespace std;

#include "NetSocket.h"

#ifndef WINAPI
	#include <sys/types.h>
	#include <sys/socket.h>
	#include <sys/wait.h>
	#include <netinet/in.h>
	#include <netdb.h>
	#include <arpa/inet.h>


#ifdef __ANDROID__

#include <fcntl.h>

#elif defined(PLATFORM_BBX)
#include <fcntl.h>

#else
	#include <sys/fcntl.h>
#endif


#include <sys/ioctl.h>
#define INVALID_SOCKET  (~0)
#define rt_closesocket(x) close(x)

#if defined(RT_WEBOS_ARM) || defined(__ANDROID__) || defined (RTLINUX)
	#include <linux/sockios.h>
	#include <errno.h>

#elif defined (PLATFORM_FLASH)

#include <sys/sockio.h>
#include <sys/errno.h>

#else

	#include <sys/sockio.h>
#endif

#else

#ifndef ECONNREFUSED
	#define ECONNREFUSED            WSAECONNREFUSED
#endif

	#define rt_closesocket(x) closesocket(x)

#endif

NetSocket::NetSocket()
{
	m_socket = INVALID_SOCKET;
	m_bWasDisconnected = false;
}

NetSocket::~NetSocket()
{
	Kill();
}

#define C_NET_MAXHOSTNAME 254

void NetSocket::Kill()
{
	m_bWasDisconnected = false;

	if (m_socket != INVALID_SOCKET)
	{
		#ifdef _DEBUG
		//LogMsg("Killed socket %d", m_socket);
		#endif
		//rt_closesocket(m_socket);
		m_socket = INVALID_SOCKET;
	}

	m_readBuffer.clear();
	m_writeBuffer.clear();

}
bool NetSocket::Init( string url, int port )
{
	Kill();
	//connect to another one

	m_idleTimer = SDL_GetTicks();
	struct sockaddr_in sa;
	struct hostent     *hp;

    #ifdef __ANDROID__
        struct addrinfo hints, *servinfo, *p;
        int rv;

        memset(&hints, 0, sizeof hints);
        hints.ai_family = AF_UNSPEC; // use AF_INET6 to force IPv6
        hints.ai_socktype = SOCK_STREAM;
        hints.ai_flags = AI_CANONNAME;
        stringstream strs;
        strs << port;
        string temp_str = strs.str();

        if ((rv = getaddrinfo(url.c_str(), temp_str.c_str(), &hints, &servinfo)) != 0)
        {
            //SDL_Log("getaddrinfo: %s", gai_strerror(rv));
            return false;
        }

        // loop through all the results and connect to the first we can
        for(p = servinfo; p != NULL; p = p->ai_next)
        {
            if ((m_socket = socket(p->ai_family, p->ai_socktype,p->ai_protocol)) == -1)
            {
                //SDL_Log("%d",errno);
            }
            else
            {
                if (connect(m_socket, p->ai_addr, p->ai_addrlen) == -1)
                {
                    close(m_socket);
                }
                else
                {
                    freeaddrinfo(servinfo);
                    break;
                }
            }
        }
    #else
        if ((hp= gethostbyname(url.c_str())) == NULL)
        {
                    /* do we know the host's */
            #ifndef PLATFORM_BBX
                    //no errno on bbx.  Wait, why am I even setting this?  Does this matter?
                    errno= ECONNREFUSED;                       /* address? */
            #endif
            return false;                                /* no */
        }

        memset(&sa,0,sizeof(sa));
        memcpy((char *)&sa.sin_addr,hp->h_addr,hp->h_length);     /* set address */
        sa.sin_family= hp->h_addrtype;
        sa.sin_port= htons((u_short)port);

        if ((m_socket= (int)socket(hp->h_addrtype,SOCK_STREAM,0)) < 0)     /* get socket */
        {
            return false;
        }
        #ifdef WINAPI
            #if !defined(__WINPHONE__)
                u_long iMode = 0;
                ioctlsocket(m_socket, FIOASYNC, &iMode);
                WSAAsyncSelect(m_socket, GetForegroundWindow(), WM_USER+1, FD_READ);
                WSAAsyncSelect(m_socket, GetForegroundWindow(), WM_USER+1, FD_WRITE);
                WSAAsyncSelect(m_socket, GetForegroundWindow(), WM_USER+1, FD_CONNECT);
                WSAAsyncSelect(m_socket, GetForegroundWindow(), WM_USER+1, FD_OOB);
            #endif
        #else
            fcntl (m_socket, F_SETFL, O_NONBLOCK);
        #endif

        int ret = connect(m_socket,(struct sockaddr *)&sa,sizeof sa);

        if (ret == -1)
        {
            SDL_Log("Couldn't open socket.");
            return false;
        }
    #endif
    return true;
}

bool NetSocket::InitHost( int port, int connections )
{
	Kill();

	sockaddr_in sa;

	memset(&sa, 0, sizeof(sa));

	sa.sin_family = PF_INET;
	sa.sin_addr.s_addr = htonl(INADDR_ANY);
	sa.sin_port = htons(port);
	m_socket = (int)socket(AF_INET, SOCK_STREAM, 0);
	if (m_socket == INVALID_SOCKET )
	{
		//LogMsg("socket command: INVALID_SOCKET");
		std::cout << "socket command: INVALID_SOCKET" << std::endl;;
		return false;
	}

	//u_long arg = 1;


	//ioctlsocket(m_socket, FIONBIO, &arg);

	/* bind the socket to the internet address */
	if (::bind(m_socket, (sockaddr *)&sa, sizeof(sockaddr_in)) == SOCKET_ERROR)
	{
		//rt_closesocket(m_socket);
		Kill();
		std::cout << ("bind: INVALID_SOCKET") << std::endl;

		return false;
	}


#ifdef WINAPI

	//u_long iMode = 0;
	//ioctlsocket(m_socket, FIOASYNC, &iMode);
    ULONG NonBlock;

	NonBlock = 1;
	if (ioctlsocket(m_socket, FIONBIO, &NonBlock) == SOCKET_ERROR)
	{
		std::cout << ("ioctlsocket() failed \n") << std::endl;
		return false;
	}

	#if !defined(__WINPHONE__)
	//u_long iMode = 0;
	//ioctlsocket(m_socket, FIOASYNC, &iMode);
	WSAAsyncSelect(m_socket, GetForegroundWindow(), WM_USER+1, FD_READ);
	WSAAsyncSelect(m_socket, GetForegroundWindow(), WM_USER+1, FD_WRITE);
	WSAAsyncSelect(m_socket, GetForegroundWindow(), WM_USER+1, FD_CONNECT);
	WSAAsyncSelect(m_socket, GetForegroundWindow(), WM_USER+1, FD_OOB);
	#endif


#else
	//int x;
	//x=fcntl(m_socket,F_GETFL,0);
	//fcntl(m_socket,F_SETFL,x | O_NONBLOCK);
	fcntl(m_socket, F_SETFL, O_NONBLOCK);


#endif


	listen(m_socket, connections);
	return true;
}


void NetSocket::SetSocket( int socket )
{
	Kill();
	m_socket = socket;
	m_idleTimer = SDL_GetTicks();
#ifndef WINAPI
	fcntl(m_socket, F_SETFL, O_NONBLOCK);
#endif

}

string NetSocket::GetClientIPAsString()
{
	if (m_socket == INVALID_SOCKET) return "NOT CONNECTED";

	sockaddr_in addr;
#ifdef WIN32
	//avoid needing ws2tcpip.h
	int addrsize = sizeof(addr);
#else
	//linux
	socklen_t addrsize = sizeof(addr);
#endif

	int result = getpeername(m_socket, (sockaddr*) &addr, &addrsize);
	//printf("Result = %d\n", result);
	char* ip = inet_ntoa(addr.sin_addr);
	int port = addr.sin_port;
	//printf("IP: %s ... PORT: %d\n", ip, port);
	return ip;

}
void NetSocket::Update()
{
	UpdateRead();
	UpdateWrite();
}

void NetSocket::UpdateRead()
{
	if (m_socket == INVALID_SOCKET) return;

	vector <char> buff;
	buff.resize(1024);
	int bytesRead;

	do
	{
	    //std::cout << "AquiL" << std::endl;
		bytesRead = ::recv (m_socket, &buff[0], buff.size(), 0);
        //std::cout << "AquiL2" << std::endl;
		if (bytesRead == 0)
		{
			//all done

			if (!m_bWasDisconnected)
			{
				#ifdef _DEBUG
					std::cout << ("Client disconnected") << std::endl;
				#endif
				m_bWasDisconnected = true;
			}
			return;
		}

		if (bytesRead == -1)
		{
            //not ready
			return;
		}

		//copy it into our real lbuffer
		m_readBuffer.insert(m_readBuffer.end(), buff.begin(), buff.begin()+bytesRead);
		#ifdef _DEBUG
		//LogMsg("Read %d bytes", bytesRead);
#ifdef WIN32
		//LogMsg(&buff[0]);  //can't really do this, because %'s will crash it
		//OutputDebugString(&buff[0]);
		//OutputDebugString("\n");
#endif
		#endif
		m_idleTimer = SDL_GetTicks();

	} while (bytesRead >= int(buff.size()));

}

void NetSocket::UpdateWrite()
{

	if (m_socket == INVALID_SOCKET || m_writeBuffer.empty()) return;

	int bytesWritten = ::send (m_socket, &m_writeBuffer[0], m_writeBuffer.size(), 0);

	if (bytesWritten <= 0)
	{
		//socket probably hasn't connected yet
		return;
	}
	m_writeBuffer.erase(m_writeBuffer.begin(), m_writeBuffer.begin()+bytesWritten);
	m_idleTimer = SDL_GetTicks();

#ifdef _DEBUG
	//LogMsg("wrote %d, %d left", bytesWritten, m_writeBuffer.size());
#endif
}

void NetSocket::Write( const string &msg )
{
	if (msg.empty()) return;
	m_writeBuffer.insert(m_writeBuffer.end(), msg.begin(), msg.end());
	UpdateWrite();
}

void NetSocket::Write( char *pBuff, int len )
{
	m_writeBuffer.resize(m_writeBuffer.size()+len);
	memcpy(&m_writeBuffer[m_writeBuffer.size()-len], pBuff, len);

	UpdateWrite();

}



int NetSocket::GetIdleTimeMS()
{
	return SDL_GetTicks()-m_idleTimer;
}
#endif
