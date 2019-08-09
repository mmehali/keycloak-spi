# Document

## Deployment
ไปที่Folder ino-lab ที่มีไฟล์ pom
build mavenเสร็จแล้ว นำไฟล์ .jarที่ได้ไปไว้ใน

[keycloak_home_directory]\standalone\deployments

## Rest endpoint

### /mygroup
[keycloak_home_uri]/auth/realms/[realms name]/ino/mygroup

ino = ID ที่กำหนดใน ino_lab/INORealmResourceProviderFactory.java
mygroup = Pathที่กำหนดใน ino_lab/INORealmResourceProvider.java
[method]
GET = Get group info
POST = Join group
Delete = Leave group
### /invitetoken
[Keycloak_HOME]/auth/realms/[realms name]/ino/invitetoken

[method]
POST = ขอ invite token[JWT]

## Invite token

### ใช้ token โดย
"GET", "[Keycloak_HOME]/auth/realms/[realms name]/login-actions/action-token?key="+[TOKEN],
### Handler token
TOKEN_TYPE ใน ino_lab/InviteToken.java ต้องตรงกับ ID ใน ino_lab/InviteTokenHandlerFactory.java
ซึ่งหากตรงดันเมื่อเรียกผ่าน Endpoint 
action-token  จะไปทำตามที่เราเขียนไว้ใน method handleToken ใน ino_lab/InviteTokenHandler.java


