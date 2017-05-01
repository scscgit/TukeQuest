# TukeQuest
School project made in Java using libGDX for a game development course.

This is a vertical platformer, a jumping game inspired by a game called IcyTower. The student represented by player strives to finish Quests, or in other words assignments from his teachers, over the course of all his semesters. Implementation consists of mechanics that motivate player to run as fast as possible, using various boosts gained from sprinting or from randomly collected items. There is a feedback loop created using flames, representing FX grade from the semester, that always increase their velocity, unless the player successfully completes short-term goals - but will slightly slow down when he is nearby. Together with gravity to punish slow players, this pattern balances the game logic. The main motivation to play further more than once is the score system, which uses jumping combo streaks and dynamic score modifiers.

## Members of the development team IDDQD

Maroš \([MarosPataky](https://github.com/MarosPataky)\)  
Štefan \([scscgit](https://github.com/scscgit)\)  
Jakub \([JakubRuzicka](https://github.com/JakubRuzicka)\)  
Jozef \([jozo5610](https://github.com/jozo5610)\)  

## Project installation

Project can be directly imported as a Gradle project in [IntelliJ IDEA](https://www.jetbrains.com/idea/). Alternatively, to setup Android SDK location and generate IntelliJ IDEA Project based on .ipr file under Windows, you can run setup_idea_android.bat.
