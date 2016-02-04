#!/usr/bin/perl -wT
use strict;
use warnings;
use CGI ':standard';

my $sucess = 1;

#opening the members file
open (LOG, "<members.csv") || error(0);
my @data = <LOG>;
close(LOG);

#getting used usernames
my @users;
for(my $i=0; $i<@data; $i++){
	my @userData = split(/ /,$data[$i]);
	if(@userData < 3 && length($data[$i]) > 0){
		error(1);
	}
	push(@users, $userData[1]);
}

#param import
my $rRealName = escapeHTML(param('realname'));
my $rUserName = escapeHTML(param('username'));
my $rPassword = escapeHTML(param('password'));
my $rPasswordConfirm = escapeHTML(param('password2'));

#param check
if(length($rRealName) == 0 || length($rUserName) == 0 || length($rPassword) == 0 || length($rPasswordConfirm) == 0){
	$sucess = 2;
}
elsif ($rPasswordConfirm ne $rPassword){
	$sucess = 3;
}

#username check
for(my $i=0; $i<@users; $i++){
	if($users[$i] eq $rUserName){
		$sucess = 4;
		last;
	}
}

#write to user file if params are good
if($sucess == 1){
	open(LOG, ">>members.csv") || error(2);
	print(LOG $rRealName." ".$rUserName." ".$rPassword);
	close(LOG);
}

#html output
print "Content-type: text/html\n\n";
print '<!DOCTYPE html><html><head><title>';

if($sucess == 1){
	print 'NSF-Success';
}
else {
	print 'NSF-Failure';
}

print '</title></head>';
print '<body bgcolor="yellow">';

if($sucess == 1){
	print '<center><h3>NASCAR SUPER FRIENDS IS A GGGGOOO!</h3></center>';
	print '<p>Go to the <a href="/~hkoivu/web/comp206/welcome.html">welcome page</a>.</p>';
}
elsif($sucess == 2){
	print '<center><h3>Please fill all fields.</h3></center>';
	print '<p>Go <a href="/~hkoivu/web/comp206/new.html">back</a>.</p>';
}
elsif($sucess == 3){
	print '<center><h3>Your password fields does not match!</h3></center>';
	print '<center><img src="/~hkoivu/web/comp206/img/bobby.jpg" height="20" width="20"/></center>';
	print '<p>Go <a href="/~hkoivu/web/comp206/new.html">back</a>.</p>';
}
elsif($sucess == 4){
	print '<center><h3>Your requested username has already been taken.</h3></center>';
	print '<p>Go <a href="/~hkoivu/web/comp206/new.html">back</a>.</p>';
}
else {
	print '<center><h3>An error occurred</h3></center>';
}

print '<p>';
print '</p>';

print '</body>';
print '</html>';

sub error {
	print "Content-type: text/html\n\n";
	if ($_[0] == 0) {
		print "Error reading file";
	}
	elsif ($_[0] == 1) {
		print "File is broken";
	}
	elsif ($_[0] == 2) {
		print "Error writing file";
	}
	exit;
}
