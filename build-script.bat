@echo off

rem Define a variable
set project_path=C:/code/test/user-account-project/


echo Building Project eureka-server
cd "%project_path%eureka-server"
call mvn clean package

echo Building Project config-server
cd "%project_path%config-server"
call mvn clean package

echo Building Project common-module
cd "%project_path%common-module"
call mvn clean package

echo Building Project user-ws
cd "%project_path%user-ws"
call mvn clean package

echo Building Project account-ws
cd "%project_path%account-ws"
call mvn clean package

echo Building Project account-statement-ws
cd "%project_path%account-statement-ws"
call mvn clean package

echo Building Project api-gateway-ws
cd "%project_path%api-gateway-ws"
call mvn clean package

echo All projects built successfully

pause
