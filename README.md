# agario_clone

agar.io 클론 코딩

## messages

### object
1. User
```
{
    "username": String
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

## connection url
`/?id=${id}&username={username}`

username은 사용자로부터 입력받은 값, id는 클라이언트가 UUID로 생성하여 서버로 던져준다.

### message type

**incoming message** (서버가 받는 메세지)

- POSITION_CHANGED
    - what : user id + current position
    
```json
{
  "type": "POSITION_CHANGED",
  "body": {
    "position": {"x": ?, "y": ?}
  }
}
```

- MERGE (user가 user를 먹는 행위)
    - what : 먹는 사람 id / 먹히는 사람 id
    
```json
{
  "type": "MERGE",
  "body": {
    "colony_id": ? 
  }
}
```
    
- EAT (user가 바닥에 깔려 있는 먹이를 먹는 행위)
    - what : 먹는 사람 id / 먹히는 먹이 id
    
```json
{
  "type": "EAT",
  "body": {
    "prey_id": ? 
  }
}
```
    
**outgoing message** (서버가 보내는 메세지)

- JOIN
    - who : roo 안의 모든 유저
    - when : 맨 처음 room에 접속했을 때
    - what : 새로 생성된 User 객체
    
```json
{
  "type": "JOIN",
  "body": {
    "new_user": User
  }
}
```

- OBJECTS
    - who : room 안의 모든 유저
    - when : POSITION_CHANGED 메세지가 들어왓을 때
    - what : 모든 유저 list + 모든 먹이 list
    
```json
{
  "type": "OBJECTS",
  "body": {
    "users": [user1, user2, user3, ...],
    "preys": [prey1, prey2, prey3, ...] 
  }
}
```

- MERGED
    - who : 모든 유저
    - when : 먹었을 때
    - what : 먹고 나서의 위치 + 원의 크기
     
```json
{
  "type": "MERGED",
  "body": {
    "position": {"x": ?, "y": ?},
    "radius": ? 
  }
}
```


- WAS_MERGED
    - who : 먹힌 유저
    - when : 먹혔을 때
    - what : empty
    
```json
{
  "type": "WAS_MERGED",
  "body": {}
}
```

- EATED
    - who : 모든 유저
    - when : 먹었을 때
    - what : 먹고 나서의 위치 + 원의 크기
    
```json
{
  "type": "WAS_MERGED",
  "body": {
    "position": {"x": ?, "y": ?},
    "radius": ?
  }
}
```
    
- SEED
    - who : room 안의 모든 유저
    - when : room 안의 먹이가 x개 이하로 떨어졌을 때
    - what : 새롭게 추가된 먹이 List
    
```json
{
  "type": "SEED",
  "body": {
    "preys": [prey1, prey2, prey3, ...]
  }
}
```