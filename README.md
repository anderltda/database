# database

# Command Sonar
mvn clean install verify sonar:sonar \            
  -Dsonar.projectKey=database \
  -Dsonar.host.url=http://localhost:9001 \
  -Dsonar.login=sqp_c2f3ba5acdcb4499b4a2bca6b92cbcd98f8c0444