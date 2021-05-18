# agario_clone

agar.io 클론 코딩

## messages

### object
1. User
```
{
    "displayName": String
    "id": UUID
    "position": (Double, Double)
    "radius": Double
}
```

2. Prey
```
{
    "id": UUID
    "position": (Double, Double)
    "radius": Double
}
```

### message type

**incoming message** (서버가 받는 메세지)

- POSITION_CHANGED
    - what : user id + current position

- MERGE (user가 user를 먹는 행위)
    - what : 먹는 사람 id / 먹히는 사람 id
    
- EAT (user가 바닥에 깔려 있는 먹이를 먹는 행위)
    - what : 먹는 사람 id / 먹히는 먹이 id
    
**outgoing message** (서버가 보내는 메세지)

- JOIN
    - who : 새로 접속한 유저
    - when : 맨 처음 room에 접속했을 때
    - what : 초기 위치

- POSITION
    - who : room 안의 모든 유저
    - when : 실시간 (매 틱마다)
    - what : 모든 유저 list + 모든 먹이 list

- MERGED
    - who : 모든 유저
    - when : 먹었을 때
    - what : 먹고 나서의 위치 + 원의 크기

- WAS_MERGED
    - who : 먹힌 유저
    - when : 먹혔을 때
    - what : empty

- EATED
    - who : 모든 유저
    - when : 먹었을 때
    - what : 먹고 나서의 위치 + 원의 크기
    
- SEED
    - who : room 안의 모든 유저
    - when : room 안의 먹이가 x개 이하로 떨어졌을 때
    - what : 새롭게 추가된 먹이 List

### message format
```
{
    "type": String // JOIN / POSITION / MERGED 등등
    "body": { ~~ } // TBD
}
```