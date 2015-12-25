import os
toPrint = []
for files in os.walk("./Math"):
    for fil in files[2]:
        if ".java" in fil:
            toPrint.append(files[0][2:] + "/" + fil)
with open("toCompile.txt","w") as f:
    for line in toPrint:
        f.write(line+"\n")