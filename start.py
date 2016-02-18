from subprocess import call
call('git add *',shell=True)
call('git commit ',shell=True)
call('git push origin master',shell=True)
