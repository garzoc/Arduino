from subprocess import call
call('git add *',shell=True)
call('git commit ',shell=True)
call('git push origin master',shell=True)
call('garzoc',shell=True)
call('ostcake6',shell=True)
