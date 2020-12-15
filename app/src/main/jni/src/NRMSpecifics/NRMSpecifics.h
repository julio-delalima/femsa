#ifndef INC_01SABROSITA_NRMSPECIFICS_H
#define INC_01SABROSITA_NRMSPECIFICS_H

#include <sstream>
#include "../Network/NetworkManager.h"
#include "project.h"

class NRMSpecifics
    {
public:
    int projectType;
    int dataVersion;
    stringstream specificData;

    virtual bool networkRequest(NetworkManager &netManager);

    virtual void networkResponse(NetworkManager &netManager);

    virtual void modifyGameProject(project &currentProject);

    virtual void saveCurrentVersion();
    };


#endif
