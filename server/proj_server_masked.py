import socket
from tensorflow import keras
from keras.layers.embeddings import Embedding
from konlpy.tag import Okt
from numpy.lib.arraypad import pad
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
from tensorflow.keras.utils import to_categorical 
import numpy as np
#전처리 과정에 필요

from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, LSTM
#모델 설계에 필요

import sqlite3
import json
conn = sqlite3.connect("proj.db",isolation_level=None) #즉시 반영 

c=conn.cursor()


host = '*.*.*.*' # 호스트 IP 마스킹 처리
port = 8080            # 포트번호를 임의로 설정해주세요

server_sock = socket.socket(socket.AF_INET)
server_sock.bind((host, port))
server_sock.listen(1)

out_data = None
rcv_msg=None
client_sock=None

okt=Okt()

train_data_location = "server/train_data.csv" # 경로 수정
f1 = open(train_data_location,'r',encoding='utf-8-sig')
y=[]
stop_words = '고 지 은 한 추천 해 줘 맛 추천해줘 해줘 메뉴'    #불용어들  
taste_arr =["0","매운맛","신맛","단맛","고소한맛"]

stop_words=set(stop_words.split(' '))
word_set=[] #sequences
lines = f1.readlines()
for line in lines:
    line = line.split(',')
    word_tokens = okt.morphs(line[0])   #형태소 별로 분류(토큰화)
    result = [word for word in word_tokens if not word in stop_words]   #불용어 제거
    word_set.append(result)
    y.append(line[1].replace('\n',''))

tokenizer = Tokenizer()
tokenizer.fit_on_texts(word_set)
vocab_size = len(tokenizer.word_index) + 1 #인덱스는 1부터 시작하지만 패딩을위한 0을 고려하여 +1
#print(word_set)
word_set = tokenizer.texts_to_sequences(word_set) #인코딩

max_len = max(len(l) for l in word_set) #모든 샘플에서 길이가 가장 긴 샘플의 길이
#print('샘플의 최대 길이 : {}'.format(max_len))
#print(word_set)
word_set = pad_sequences(word_set,maxlen=max_len,padding='pre') #출력 차원수 같게하기 위해 패딩
#print(word_set) #인코딩 + 패딩 끝난 훈련 샘플 
#print(y) #레이블
#전처리 작업 끝
y = to_categorical(y,num_classes=vocab_size) #원-핫 인코딩
#print(y)

embedding_dim = 10
hidden_units=128

model = Sequential()
model.add(Embedding(vocab_size,embedding_dim))
model.add(LSTM(hidden_units))
model.add(Dense(vocab_size,activation='softmax'))
model.compile(loss='categorical_crossentropy',optimizer='adam',metrics=['accuracy'])
model.fit(word_set,y,epochs=200,verbose=2) # 훈련


while True: #안드로이드에서 연결 버튼 누를 때까지 기다림
    print("기다리는 중..")
    client_sock, addr = server_sock.accept() # 연결 승인
    
    if client_sock: #client_sock 가 null 값이 아니라면 (연결 승인 되었다면)
        print('Connected by?!', addr) #연결주소 print
        in_data = client_sock.recv(1024) #안드로이드에서 "refresh" 전송
        print('rcv :', in_data.decode("utf-8"),'len : ', len(in_data)) #전송 받은값 디코딩
        rcv_msg = str(in_data.decode("utf-8"))
        #time.sleep(5)
        if rcv_msg == "exit" :
            break;
        in_data = rcv_msg
        in_word_tokens = okt.morphs(in_data)   #형태소 별로 분류(토큰화)
        in_word = [word for word in in_word_tokens if not word in stop_words]   #불용어 제거
        print(in_word)
        encoded = tokenizer.texts_to_sequences(in_word) #정수로 인코딩
        
        encoded = pad_sequences([encoded],maxlen=max_len,padding='pre') #패딩

        yhat = model.predict(encoded,verbose=0)
        yhat=np.round(yhat,2)
        processed_msg = taste_arr[np.argmax(yhat[0])]
        print(processed_msg) #처리된 메시지
        param=processed_msg,
        c.execute("SELECT json_object('name', name, 'img', img, 'price', price, 'inside', inside, 'brand', brand, 'flavour', flavour)  FROM proj_table WHERE flavour=?",param)
        fetch_obj = c.fetchall()
        # print(fetch_obj)
        # json_str = json.dumps(fetch_obj,ensure_ascii=False,indent=2)
        # print(json_str)
        fetch_obj = str(fetch_obj).replace('(','')
        fetch_obj = str(fetch_obj).replace(',)','\n')
        fetch_obj = str(fetch_obj).replace('\'','')
        print(fetch_obj)
        client_sock.send(str(fetch_obj).encode("utf-8"))

client_sock.close()
server_sock.close()
