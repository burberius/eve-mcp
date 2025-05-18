# Eve Online Model Context Protocol Server

This is a MCP server that provides data from Eve Online to AI systems like Ollama.
Currently it just uses the public data from the SDE and ESI endpoints. 

## Build and run it

```
./mvnw clean package
java -jar target/eve-mcp-0.0.1-SNAPSHOT.jar
```

## Example queries

* Is Jita a high sec system?
* Can you tell me more about Bei? Is it a safe system?
* Can you tell me how many jumps are from Jita to Hek on the safe and on the shortest route are? Are how many systems are the same on both routes?
* Were there any pod kills on the shortest route from Jita to Hek?
* How many jumps and kills were in Jita?
* Are there any incursions with a boss present?