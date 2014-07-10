
redis = require("redis")

client = redis.createClient()

client.on("error", (err) ->
  console.log("error event - " + client.host + ":" + client.port + " - " + err)

client.set("string key", "string val", redis.print)

client.hset("hash key", "hashtest 1", "some value", redis.print)

client.hset(["hash key", "hashtest 2", "some other value"], redis.print)

client.hkeys("hash key", (err, replies) ->
    if err
      return console.error("error response - " + err)

    console.log(replies.length + " replies:")
    replies.forEach((reply, i) ->
      console.log("    " + i + ": " + reply)
    )
)

client.hmset('access:codes', {userid: 'foo', code: 'bar'})

client.hgetall('access:codes', (err, reply) ->
  if err
    return console.error("error response - " + err)

  console.log(reply.code + " replies:")
  console.log("   : " + JSON.stringify(reply))
)

client.quit((err, res) ->
  console.log("Exiting from quit command."))
)