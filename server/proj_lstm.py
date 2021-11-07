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


okt=Okt()

train_data_location = "E:\\sw_proj\\sw_proj\\server\\train_data.csv"
f1 = open(train_data_location,'r',encoding='utf-8-sig')
y=[]
stop_words = '고 지 은 한 추천 해 줘 맛 추천해줘 해줘'    #불용어들 
stop_words=set(stop_words.split(' '))
word_set=[] #sequences
lines = f1.readlines()
for line in lines:
    line = line.split(',')
    word_tokens = okt.morphs(line[0])   #형태소 별로 분류(토큰화)
    result = [word for word in word_tokens if not word in stop_words]   #불용어 제거
    word_set.append(result)
    y.append(line[1].replace('\n',''))
    #print(result)
    #print()

tokenizer = Tokenizer()
tokenizer.fit_on_texts(word_set)
vocab_size = len(tokenizer.word_index) + 1 #인덱스는 1부터 시작하지만 패딩을위한 0을 고려하여 +1
#print(word_set)
word_set = tokenizer.texts_to_sequences(word_set) #인코딩
#print(tokenizer.texts_to_sequences(word_set))
#print(tokenizer.word_index)


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
model.fit(word_set,y,epochs=200,verbose=2)


taste_arr =["0","매운맛","신맛","단맛","고소한맛"]

while True:
    in_data = input("예측할 입력 문장 : ")
    if in_data == "exit":
        break
    in_word_tokens = okt.morphs(in_data)   #형태소 별로 분류(토큰화)
    print(in_word_tokens)
    in_word = [word for word in in_word_tokens if not word in stop_words]   #불용어 제거
    print(in_word)
    encoded = tokenizer.texts_to_sequences(in_word) #정수로 인코딩
    encoded = pad_sequences([encoded],maxlen=max_len,padding='pre') #패딩
    yhat = model.predict(encoded,verbose=0)
    yhat=np.round(yhat,2)
    print(taste_arr[np.argmax(yhat[0])]) #출력 원핫 벡터중 가장 큰값 으로 맛 찾기

# for value in yhat[0]:
#     if value >= max:
#         max=value

#print(np.argmax(yhat[0]))
