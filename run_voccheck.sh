# Copy all java files to root directory, so that voccheck can check them
cp ./src/client/*.java .
cp ./src/datastructures/*.java .
cp ./src/gui/*.java .
cp ./src/packets/response/*.java .
cp ./src/packets/request/*.java .
cp ./src/server/*.java .

# Run voccheck, and pipe the result to voccheck_log
voccheck 2>&1 | tee voccheck_log

# Remove all .java files, except Fake.java
ls | grep -v Fake.java | grep ".java" | xargs rm
