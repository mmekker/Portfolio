import smtplib
import zipfile
import os
from email.MIMEMultipart import MIMEMultipart
from email.MIMEBase import MIMEBase
from email import Encoders

#This program was made for CSC344 - Programming Languages
#Author: Mike Mekker
#This program takes all of the assignment files for this class,
#counts how many lines each has, display the information in a website format,
#and then zips the files and emails them to Professor Lea

#change directory and open source files
os.chdir("C:/Users/user/Documents/School/CSC344/")
a1 = open("Assignment1/A1.c", "r")
a2 = open("Assignment2/A2.lisp", "r")
a3 = open("Assignment3/A3.scala", "r")
a4 = open("Assignment4/A4.pro", "r")
a5 = open("assignment5/a5.py", "r")

#counting number of non-empty, non-comment lines
LinesA1 = 0
LinesA2 = 0
LinesA3 = 0
LinesA4 = 0
LinesA5 = 0
commentStartA1 = False
blockCommentStartA1 = False
tempA1 = False

commentStartA2 = False
blockCommentStartA2 = False
tempA2 = False

commentStartA3 = False
blockCommentStartA3 = False
tempA3 = False

commentStartA4 = False
blockCommentStartA4 = False
tempA4 = False


#Loop for A1
for line in a1:
    for ch in line:
        if ch == ' ':
            continue
        elif ch == '\n' and not blockCommentStartA1:
            LinesA1 -= 1
            break
        elif ch == '/':
            if not commentStartA1:
                commentStartA1 = True
                continue
            elif tempA1:
                blockCommentStartA1 = False
                commentStartA1 = False
                LinesA1 -= 1
                break
            else:
                LinesA1 -= 1
                commentStartA1 = False
                break
        elif ch == '*':
            if commentStartA1 and not blockCommentStartA1:
                blockCommentStartA1 = True
            elif blockCommentStartA1:
                tempA1 = True
        elif not blockCommentStartA1:
            tempA1 = False
            break
    if not blockCommentStartA1:
        LinesA1 += 1

#Loop for A2
for line in a2:
    for ch in line:
        if ch == ' ':
            continue
        elif ch == '\n' and not blockCommentStartA2:
            LinesA2 -= 1
            break
        elif ch == ';':
            LinesA2 -= 1
            break
        elif ch == '#':
            if not commentStartA2:
                commentStartA2 = True
                continue
            elif tempA2:
                blockCommentStartA2 = False
                commentStartA2 = False
                LinesA2 -= 1
                break

        elif ch == '|':
            if not blockCommentStartA2 and commentStartA2:
                blockCommentStartA2 = True
            elif blockCommentStartA2:
                tempA2 = True
        elif not blockCommentStartA2:
            tempA2 = false
            break
    if not blockCommentStartA2:
        LinesA2 += 1

#Loop for A3
for line in a3:
    for ch in line:
        if ch == ' ':
            continue
        elif ch == '\n' and not blockCommentStartA3:
            LinesA3 -= 1
            break
        elif ch == '/':
            if not commentStartA3:
                commentStartA3 = True
                continue
            elif tempA3:
                blockCommentStartA3 = False
                commentStartA3 = False
                LinesA3 -= 1
                break
            else:
                LinesA3 -= 1
                commentStartA3 = False
                break
        elif ch == '*':
            if commentStartA3 and not blockCommentStartA3:
                blockCommentStartA3 = True
            elif blockCommentStartA3:
                tempA3 = True
        elif not blockCommentStartA3:
            tempA3 = False
            break
    if not blockCommentStartA3:
        LinesA3 += 1

#Loop for A4
for line in a4:
    for ch in line:
        if ch == ' ':
            continue
        elif ch == '\n' and not blockCommentStartA4:
            LinesA4 -= 1
            break
        elif ch == '%':
            LinesA4 -= 1
            break
        elif ch == '/':
            if not commentStartA4:
                commentStartA4 = True
                continue
            elif tempA4:
                blockCommentStartA4 = False
                commentStartA4 = False
                LinesA4 -= 1
                break

        elif ch == '*':
            if not blockCommentStartA4 and commentStartA4:
                blockCommentStartA4 = True
            elif blockCommentStartA4:
                tempA4 = True
        elif not blockCommentStartA4:
            tempA4
            break
    if not blockCommentStartA4:
        LinesA4 += 1

#Loop for A5
for line in a5:
    for ch in line:
        if ch == ' ':
            continue
        elif ch == '\n':
            LinesA5 -= 1
            break
        elif ch == '#':
            LinesA5 -= 1
            break
        else:
            break
    LinesA5 += 1

#make a website with links to each source file and with number of lines for each
WEBSITE_HTML =  '''<html>
	                <a href="C:\Users\user\Documents\School\CSC344\Assignment1\A1.c">Assignment 1</a>
	                     Number of lines = %d <br>
	                <a href="C:\Users\user\Documents\School\CSC344\Assignment2\A2.lisp">Assignment 2</a>
	                     Number of lines = %d <br>
	                <a href="C:\Users\user\Documents\School\CSC344\Assignment3\A3.scala">Assignment 3</a>
	                     Number of lines = %d <br>
	                <a href="C:\Users\user\Documents\School\CSC344\Assignment4\A4.pro">Assignment 4</a>
	                     Number of lines = %d <br>
	                <a href="C:\Users\user\Documents\School\CSC344\\assignment5\\a5.py">Assignment 5</a>
	                     Number of lines = %d <br>
                </html>
            ''' % (LinesA1, LinesA2, LinesA3, LinesA4, LinesA5)

mainHTML = open("main.html", "w")
mainHTML.write(WEBSITE_HTML)
mainHTML.close()

#zip files in a zip archive names zFile
def zip_archive(name, f):
    zFile = zipfile.ZipFile(name, "w")
    for file in f:
        zFile.write(file)
    zFile.close()

file_name = "zFile.zip"
files = ["Assignment1/A1.c",
         "Assignment2/A2.lisp",
         "Assignment3/A3.scala",
         "Assignment4/A4.pro",
         "assignment5/a5.py",
         "main.html"]

zip_archive(file_name, files)

#make email with zip file attached
TO = [raw_input("Enter the email address you would like to send to.  ")]
FROM = "mikemekker2@gmail.com"
SUBJECT = "Assignment 5"

msg = MIMEMultipart()
msg['Subject'] = SUBJECT
msg['From'] = FROM
msg['To'] = ', '.join(TO)
#add attachment
part = MIMEBase('application', "zip")
part.set_payload(open("zFile.zip", "rb").read())
Encoders.encode_base64(part)
part.add_header('Content-Disposition', 'attachment; filename="zFile.zip"')
msg.attach(part)

#send email
server = smtplib.SMTP('smtp.gmail.com', 587)
server.ehlo()
server.starttls()
server.login(FROM, 'sw2paTre')
try:
    server.sendmail(FROM, TO, msg.as_string())
    print 'Email Sent.'
except:
    print 'Error Sending email'



a1.close()
a2.close()
a3.close()
a4.close()
a5.close()
server.close()