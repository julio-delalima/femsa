#ifndef MISCUTILS_H
#define MISCUTILS_H
#include <string>
#include <vector>

#ifndef SAFE_DELETE
#define SAFE_DELETE(p) { if(p) { delete (p); (p)=NULL; } }
#endif

#define PI (3.14159265359f)
#define TO_DEGREES(x) ((x)*180.0f/PI)
#define TO_RADIANS(x) ((x)*PI/180.0f)

#define UTIL_SPACES " \t\r\n"

std::string StripWhiteSpace(const std::string &s);
std::string TrimLeft (const std::string & s, const std::string & t = UTIL_SPACES);
std::string TrimRight (const std::string & s, const std::string & t = UTIL_SPACES);
int StringToInt(const std::string &s);

/**
* Splits \a str into multiple parts delimited by \a delimiter.
*
* Examples:
*
* \code
* string str("foo_bar_baz");
* StringTokenize(str, "_"); // Returned parts are "foo", "bar" and "baz"
* \endcode
*
* \code
* string str("foo***bar****baz");
* StringTokenize(str, "**"); // Returned parts are "foo", "*bar", "" (an empty string) and "baz"
* \endcode
*/
std::vector<std::string> StringTokenize(const std::string& str, const std::string& delimiter);

std::string GetFileExtension(std::string fileName);
std::string ModifyFileExtension(std::string fileName, std::string extension);
std::string GetFileNameFromString(const std::string &path);
void StringReplace(const std::string& what, const std::string& with, std::string& in);
std::string NumberToString ( int Number );
std::string SeparateStringSTL(std::string input, int index, char delimiter);
bool SeparateString (const char str[], int num, char delimiter, char *return1);

/**
 * Returns a random number limited by \a range.
 *
 * If \a range is positive the returned value is on the interval [0, range[.
 *
 * If \a range is negative the returned value is on the interval [0, -range[.
 */
int Random(int range);
/**
 * Returns a random number between \a rangeMin and \a rangeMax.
 * The range includes \a rangeMin but not \a rangeMax, in other words
 * the returned value is on the interval [rangeMin, rangeMax[.
 *
 * This function is meant to be used so that \a rangeMin < \a rangeMax.
 * However the behaviour is well defined for other combinations as well.
 * - If \a rangeMin equals \a rangeMax then \a rangeMin is returned.
 * - If \a rangeMin != \a rangeMax the returned value is on the interval
 *   [rangeMin, rangeMin+abs(rangeMax-rangeMin)[.
 */
int RandomRange(int rangeMin, int rangeMax);
/**
 * Returns a random floating point number between \a rangeMin and \a rangeMax.
 *
 * The behaviour of this function is similar to RandomRange(). The notes on
 * that function apply to this function too.
 *
 * \see RandomRange()
 */
float RandomRangeFloat(float rangeMin, float rangeMax);

std::string FilterToValidAscii(const std::string &input, bool bStrict);

void RemoveFile( std::string fileName);

bool FileExists(const std::string &fName);


#endif // MISCUTILS_H
