import os
toPrint = []
lines = []
for files in os.walk("./West/Math"):
    for fil in files[2]:
        if ".java" in fil:
            toPrint.append(files[0][2:] + "/" + fil)
            with open(files[0][2:] + "/" + fil,"r") as f:
                for line in f.readlines():
                    if len(line.strip()) != 0 and "/**" not in line.strip() and "*" not in line.strip()[0]:
                        lines.append(line.strip())
chars = sum([len(x) for x in lines])
print("\n------\n#lines:",len(lines),"\n#chars:",chars,"\n#chars per line: " + str(chars / len(lines)))
larg = 0
for line in lines:
    if len(line) > larg:
        larg = len(line)
print("largest line:",larg,"\n------\n")
with open("toCompile.txt","w") as f:
    for line in toPrint:
        f.write(line+"\n")