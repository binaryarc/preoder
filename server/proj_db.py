
import sqlite3 
import json
conn = sqlite3.connect("proj.db",isolation_level=None) #즉시 반영 

c=conn.cursor()


#VALUES(name text PRIMARY KEY, img text, price text, inside  text, brand text)")
# c.execute("INSERT INTO proj_table\
#     VALUES('빅맥','https://www.mcdonalds.co.kr/upload/product/pcList/1583727841393.png','4,600','치즈','맥도날드')")
param = '매운맛',

c.execute("SELECT json_object('name', name, 'img', img, 'price', price, 'inside', inside, 'brand', brand, 'flavour', flavour) AS recommends  FROM proj_table WHERE flavour=?",param)


#print(c.fetchone())
#print(c.fetchall())


#json_str = json.dumps(c.fetchall(),ensure_ascii=False,indent=2)
fetch_obj = c.fetchall()
fetch_obj = str(fetch_obj).replace('(','')
fetch_obj = str(fetch_obj).replace(',)','\n')
fetch_obj = str(fetch_obj).replace('\'','')

print(fetch_obj)
# json_str = json.dumps(fetch_obj,ensure_ascii=False,indent=2)
# print(json_str)