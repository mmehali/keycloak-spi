import {keycloak} from '../main'
var userList  = function() {
    const authUrl = 'https://keycloak.demo.web.meca.in.th/auth'
    const realmName = '/dev'
    var url = authUrl+realmName+'/users/2354bef4-4d84-41ba-9a9c-a50087283893';

    var req = new XMLHttpRequest();
    req.open('GET', url, true);
    req.setRequestHeader('Accept', 'application/json');
    req.setRequestHeader('Authorization', 'Bearer ' + keycloak.token);

    req.onreadystatechange = function () {
        if (req.readyState == 4) {
            if (req.status == 200) {
                alert('Success');
            } else if (req.status == 403) {
                alert('Forbidden');
            }
        }
    }

    req.send();
}

export default userList()