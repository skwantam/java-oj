for i in range(200):
    fmt = "part-{0}-c67770f2-6ef1-46da-b222-1bd863eb546f-c000.csv".format(str(i).rjust(5, '0'))
    print("""load data infile 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/""" + fmt + """' into table test fields terminated by ',';""")
