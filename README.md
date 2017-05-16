# MowItNow
## Description
Small command line interface (CLI) to pilot mowers into a field.  
Given a field and a mower with initial position, we want to apply a set of
instruction on it and get the final position.

## Field
Represents the area into which the mower will move.  
It is described with a lenght and an height.
## Mower
Is defined with coordinate (X, Y) and an orientation {N,E,W,S}.  
The mower must start inside the defined field.  
## Instruction
A set of move amongst {A,D,G}.
* A: Move forward
* D: Turn right
* G: Turn left

## Usage
`java -jar mowitnow.jar --instruction [path_to_instruction_file]`

### Prerequisities
JDK 8 must be available on the running machine.

## Instruction file
The first line is the field dimension.  
The next set of lines are grouped by two.    
First element being the mower initial coordinates and orientation.    
Second element being the set of instruction.   

Mower must start inside the field.    
Instruction should follow the pattern {A,D,G}   


Valid instruction file:  
```
5 5
1 2 N  
GAGAGAGAA  
3 3 E  
AADAADADDA 
```


 