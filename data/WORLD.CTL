version=1
name=World
description=World of Mindmelt
num=1
--
// entry to Omgras castle
filename=omgra0
desc=Entering Omgra's Castle
from=109,9,154,110,10,155
to=2,9,40,N
-
//Short cut to omgra
filename=world
desc=Taking the Short Cut to Omgra's Castle
from=118,8,115
to=109,9,153
-
// Aporto
filename=aporto
desc=Entering the Village of Aporto
from=172,9,85,172,10,85
to=21,9,45,N
-
//Abbey
filename=abbey
desc=Entering the Abbey of Monks
from=220,9,25,220,10,25
to=21,9,39,N
-
//Cave
filename=cave
desc=Entering the Hermit's Cave
from=222,9,87
to=10,9,19,N
-
//Dorma
filename=dorma
desc=Entering the Dorma Prison
from=22,9,220
to=21,9,39,N
-
//Hove
filename=hove
desc=Entering the Village of Hove
from=214,9,118,214,10,118
to=22,9,41,N
-
//Lakile
filename=lakile
desc=Entering Lake Island
from=85,9,55,85,10,55
to=19,9,40,N
-
//Pennin
filename=pennin
desc=Entering the Village of Pennin
from=19,9,22,19,10,22
to=21,9,41,N
-
//Milloc
filename=milloc
desc=Entering the Village of Milloc
from=88,9,100,88,10,100
to=21,9,39,N
-
//Kiston
filename=kiston
desc=Entering the Kingdom of Kiston
from=211,9,220
to=23,9,42,N
-
//Under (tree)
filename=under
desc=Entering the Underground Cave
from=22,9,91,22,10,91
to=21,9,37,N
-
//Under (waterfall)
filename=under
desc=Entering the Underground Cave
from=62,9,91,62,11,91
to=37,9,20,W
--
// control
// Stone circle
Activate 40,36,1
Message Mage
-
Activate 42,37,1
Message Wizard
-
Activate 43,39,1
Message Prisoner
-
Activate 42,41,1
Message King
-
Activate 40,42,1
Message Werebeast
-
Activate 38,41,1
Message Monk
-
Activate 37,39,1
Message Hermit
-
Activate 38,37,1
Message Evil One
-
// Enchanted wood
Activate 14,37,1
Message Beware - Enchanted Wood
-
Activate 14,22,1
Message Beware - Enchanted Wood
-
//Places to enter
Activate 6,7,1
Message Pennin Village
-
Activate 29,19,1
Message Lakile House
-
Activate 74,9,1
Message Monks Abbey
-
Activate 61,29,1
Message Aporto Village
-
Activate 31,33,1
Message Milloc Village
-
Activate 8,74,1
Message Dorma Prison
-
Activate 37,52,1
Message Dark Castle
-
Activate 71,74,1
Message Kiston Palace
-
Activate 72,40,1
Message Hove Village
-
Activate 75,30,1
Message Hermits Cave
-
// Quoxonia - zap
Reply 67,1
ObjectIn 102,67
GetObject 102
-
// Finbar - stun
Reply 68,1
ObjectIn 101,68
GotObject 109
GetObject 101
MoveObjectInto 109,68
-
// Fingle - Overview
Reply 69,1
ObjectIn 105,69
GotObject 110
GetObject 105
MoveObjectInto 110,68
--
//Talk
id=67
talkfirst=1
sayfirst=1
-
name,Don't you Know?
job,A poor old woman
witch,Be carefull what you say!
poor,But not in wits!
old,Older than you maybe
wits,I know many things
thing|things,Like spells
spell,Whats my name?
quox|quoxonia,You can have a zap spell,1
rune,I know of no rune FOOL
greeting,I'm a poor old woman
dontknow,Wibble wobble!
bye,Nice Chat
-
id=68
sayfirst=1
-
name,Finbar
job,A bard
bard,I collect stories
stories|story,I'll swap a story for a spell
spell|swap|stun,A stun spell,1
greeting,I'm Finbar the Bard
-
id=69
-
name,Fingle
job,I'm a tinker by trade
tink|tinker,Tinkers like their food
food,I collect recipes
reci|recipe,For cooking food
trade,I'll trade a recipe for a spell
spell,The overview spell,1
bye,Toodle Pop
-
id=32
-
name,Uptik
job,A merchant
merchant,A trader of sorts
trade,I trade from town to town
town,Like Milloc
milloc,A town in the mountains 
bye,Fair thee well
-
id=33
-
name,Rinnor
job,A traveler
travel|traveler,'Tis bad to travel now
bad|now,Since the monsters appeared
monster|monsters,Take refuge in the circle
circle|stone|magic,The magic stone circle
refuge,Monsters can't enter it
bye,Travel well
-
id=34
-
name,Mad Col
col,The Mad Twonk
job,A total Twonk
total,Yeah! Totaly
twonk,Twonks can't Jump
jump,In the park
park,Blatant it was!
blatant,What a Twonk
mad,Because of the Monsters
monsters,They pop up from nowhere
nowhere,When one stands near the stones
stone|stones,In the circle
bibble,Bibble! 
dontknow,Bibble! 
bye,Bottoms up
--
