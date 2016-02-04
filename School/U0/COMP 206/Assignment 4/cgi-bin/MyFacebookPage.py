#!/usr/bin/python
import csv
import cgi

#Says its html
print "Content-type:text/html\n\n"
me = 'me'  

#Gets input from page
form  = cgi.FieldStorage()
if  form.getvalue("me"):
    me = form.getvalue("me")

#opens members
out =  open('members.csv', 'r')
l = out.readlines()
out.close()

#Sets up HTML and Top Bar
print '<!DOCTYPE html><html lang="en-US"><title>NasCar Newsfeed</title><body background="http://www.npr.org/assets/img/2013/02/23/nascar_daytona_nationwide_auto_racing_15301727_wide-55c50c6286f877295871e17dee42881054eebf64.jpg">'
print '<input type="hidden" name="me"value="'+ me +'">'
print '<center><table bgcolor="yellow" border="1"></center>'
print '<tr><td>'
print '<h4>'+ me +'</h4></td>'
print '<td><h1>NASCAR~~~~~SUPER~~~~~~FRIENDS</h1></td>	<td><form action="http://cs.mcgill.ca/~hkoivu/web/comp206/welcome.html"><input type="submit" value="Logout"></form></td></tr></table>'

#Users Section
print '<table frame="box" bgcolor="yellow"><tr><td>'
print '<form action="MyFacebookPage.py" method="POST">'
print '<input type="hidden" name="me" value="'+ me +'">'
print '<textarea name="user" placeholder="Add User..." cols="20" rows="1"></textarea></td>'
print '<td><input type="submit" value="Submit"></td>'

#Prints users 
n = 0
m = 0
t = 0
friends = []
file = open("members.csv")
for lines in file:
	info = lines.split(",")
	person = info[0].split(" ")
	if(person[1] != me):
		print '<td>'+str(person[1]) +'</td>'

	elif (person[1] == me) :
		k = 3
		while k < len(person):
			friends.append(person[k]) 
			t = t + 1
			k = k + 1
		m = n
	n = n + 1

file.close()

#Adds users
if form.getvalue("user"):
    	user = form.getvalue("user")
	l[m] = l[m].replace("\n", "")
	l[m] = l[m] +" "+ user + "\n"
   	fob = open('members.csv', 'w')
	fob.writelines(l)
	fob.close()

#Adds posts
if form.getvalue("post"):
	post = form.getvalue("post")
	a =  open('topics.csv', 'a')
	a.seek(0,0)
	a.write(me + '\n')
	a.write(post+ '\n')

print '</tr></table>'

#Feed section
print '<table border="1" bgcolor="yellow"><tr><td>'
print '<form action="MyFacebookPage.py" method="POST">'
print '<textarea name="post" placeholder="NASCAR update..." cols="50" rows="5"></textarea></td><td>'
print '<input type="submit" value="Post"></td></form></td></tr>'



#Opens feed and posts 
top = open("topics.csv", "r")
print '<tr><td>MESSAGE</td><td>USER NAME</td></tr>'

q = 0
line = top.readlines()
n = len(line)-2
limit = 0
while  0 < n and limit < 10:
	line[n] = line[n].replace("\n", "")
	friends[q] = friends[q].replace('\n',"")
	while q < len(friends):		
		if line[n] == friends[q]:
			print '<tr><td>' + line[n+1] + '</td><td>'+ line [n] +'</td></tr>'
			limit = limit + 1
			break
		q = q + 1
	q = 0
	n = n - 2

#ends HTML
print '</table>'
print '</body>'
print '</html>'


