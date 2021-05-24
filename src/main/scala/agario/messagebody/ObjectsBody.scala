package agario.messagebody

import agario.`object`.{Prey, User}

case class ObjectsBody(users: List[User], preys: List[Prey])
