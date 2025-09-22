# product-curd-backend-app

# for run  the command prompt below cmd

mvn clean spring-boot:run -Dspring-boot.run.profiles=local

# for run with debugging the command prompt below cmd

mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.arguments="--debug"

# for run the intellij terminal(Powersell) below cmd

mvn clean spring-boot:run "-Dspring-boot.run.profiles=local"

# for run with debugging  intellij terminal(Powersell) below cmd

mvn clean spring-boot:run "-Dspring-boot.run.profiles=local" "-Dspring-boot.run.arguments=--debug"
