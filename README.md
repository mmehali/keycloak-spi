
# keycloak-spi

## deploy

build mavenเสร็จแล้ว เอาไฟล์ jarไปไว้ใน

[keycloak home]\standalone\deployments

## rest endpoint

ino = ID ที่กำหนดใน ProviderFactory

mygroup = Pathที่กำหนดใน Provider

http://localhost:8080/auth/realms/[realms name]/ino/mygroup

## Custom Account Page
จะใช้ การ Deploy ในรูปแบบของ module อ้างอิง https://www.keycloak.org/docs/latest/server_development/index.html#register-a-provider-using-modules

สร้างโฟลเดอร์ ที่ KEYCLOAK_HOME/modules/keycloak/ino_lab/main/ แล้วคัดลอกไฟล์ .jar ที่ build ไว้มาวางไว้ที่นี่ และสร้างไฟล์ module.xml โดยมีเนื้อหาดังนี้

```
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.3" name="keycloak.ino_lab">
    <resources>
        <resource-root path="ino-lab-0.01b.jar"/>
    </resources>
    <dependencies>
        <module name="org.keycloak.keycloak-core"/>
        <module name="org.keycloak.keycloak-server-spi"/>
        <module name="org.keycloak.keycloak-server-spi-private"/>
        <module name="org.keycloak.keycloak-services"/>
        <module name="org.jboss.logging"/>
    </dependencies>
</module>
```

แก้ไขไฟล์ KEYCLOAK_HOME/standalone/configuration/standalone.xml จุดต่างๆ ดังนี้

- เพิ่ม SPI ให้กับ Keycloak โดยแทรกเข้าไปในแทก providers ของ subsystem ตามด้านล่าง ดังนี้
```
<subsystem xmlns="urn:jboss:domain:keycloak-server:1.1">
            <web-context>auth</web-context>
            <providers>
                <provider>
                    classpath:${jboss.home.dir}/providers/*
                </provider>
                <provider>module:keycloak.ino_lab</provider>
            </providers>
```
- และที่ subsystem เดียวกัน ตั้งค่า SPI ที่ชื่อ Account ให้ใช้ Provider ที่เราสร้างขึ้น โดยแทรกไปก่อนแทกปิดของ subsystem
```
            <spi name="account">
                <default-provider>mecas</default-provider>
            </spi>
      </subsystem>
```

แล้ว `./bin/standalone.sh` ตามเดิม ตรวจสอบหน้าจอที่รัน ว่าไม่มี error ขึ้นมา แล้ว ลองเข้าหน้า Account ที่ http://localhost:8080/auth/realms/master/account/ แล้วตรวจสอบที่หน้าจอที่รัน จะปรากฎ error message 
```
22:13:45,043 ERROR [keycloak.ino_lab.MECAsAccountProvider] (default task-1) :P
```
แสดงว่าหน้านั้นเกิดจากการเรียกใช้ SPI ที่เราสร้างขึ้นแล้ว
