;; two breeds of turtle: bugs and predators

breed [bugs bug]

bugs-own [
  red-gene    ;; gene for strength of expressing red pigment (0-100)
  blue-gene   ;; gene for strength of expressing blue pigment (0-100)
  green-gene  ;; gene for strength of expressing green pigment (0-100)
]


globals [
  total-caught     ;; running total of the total number of bugs caught
]


to setup
  clear-all
  set-default-shape bugs "moth"
  ;change-environment
  make-initial-bugs
  reset-ticks
end


to go
  if mouse-down? [
    touch-down mouse-xcor mouse-ycor
  ]
  tick
end



;;;;;;;;;;;;;;;;;;;
;; Setup Procedures
;;;;;;;;;;;;;;;;;;;

to make-initial-bugs
  create-bugs carrying-capacity [
    set size bug-size
    set red-gene (random 101)
    set blue-gene (random 101)
    set green-gene (random 101)
    setxy random-xcor random-ycor
    set-phenotype-color
  ]
end


to make-one-offspring ;; turtle procedure
  ;; three possible random mutations can occur, one in each frequency of gene expression
  ;; the max-mutation-step determines the maximum amount the gene frequency can drift up
  ;; or down in this offspring
  let red-mutation   random (max-mutation-step + 1) - random (max-mutation-step + 1)
  let green-mutation random (max-mutation-step + 1) - random (max-mutation-step + 1)
  let blue-mutation  random (max-mutation-step + 1) - random (max-mutation-step + 1)
  hatch 1 [
    set size bug-size
    set red-gene   limit-gene (red-gene   + red-mutation)
    set green-gene limit-gene (green-gene + green-mutation)
    set blue-gene  limit-gene (blue-gene  + blue-mutation)
    set-phenotype-color
    wander
  ]
end


to wander ;; turtle procedure, makes bugs wander around randomly
   rt random 360
   fd random-float (offspring-distance + 1)
end


;;;;;;;;;;;;;;;;;;;;;
;; Touch Events
;;;;;;;;;;;;;;;;;;;;;


to touch-down [touch-x touch-y]
  let prey bugs with [(distancexy touch-x touch-y) < (size / 2) ]
  if any? prey [
    set total-caught (total-caught + 1)
    ask one-of prey [ die ]
    if count bugs < carrying-capacity [
      ask one-of bugs [ make-one-offspring ]
    ]
  ]
end



;; imposes a threshold limit on gene-frequency.
;; without this genes could drift into negative values or very large values
;; (any value above 100%)
to-report limit-gene [gene]
  if gene < 0   [ report 0   ]
  if gene > 100 [ report 100 ]
  report gene
end


to change-environment
  ;; loads an image as a background from the current directory the model was launched from
   import-drawing environment
end


;;;;;;;;;;;;;;;;;;;;;;
;; Genotype to Phenotype Conversion Procedures
;;;;;;;;;;;;;;;;;;;;;;

;; convert the genetic representation of gene frequency
;; into a phenotype (i.e., a color, using an RGB list
;; instead of a standard NetLogo color)
to set-phenotype-color  ;; turtle procedure
  ;; RGB lists are expected to be in the 0-255 range,
  ;; so we must convert from the 0-100 range in the genotype
  set color (list ((red-gene   / 100) * 255)
                  ((green-gene / 100) * 255)
                  ((blue-gene  / 100) * 255))
end


; Copyright 2005 Uri Wilensky.
; See Info tab for full copyright and license.
@#$#@#$#@
GRAPHICS-WINDOW
536
10
956
451
20
20
10.0
1
10
1
1
1
0
0
0
1
-20
20
-20
20
1
1
1
time
30.0

BUTTON
297
89
352
122
NIL
setup
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

BUTTON
353
89
408
122
NIL
go
T
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

MONITOR
408
267
489
312
NIL
total-caught
0
1
11

SLIDER
5
23
141
56
carrying-capacity
carrying-capacity
0
100
15
5
1
NIL
HORIZONTAL

SLIDER
6
151
139
184
offspring-distance
offspring-distance
1
20
8
1
1
NIL
HORIZONTAL

CHOOSER
243
327
387
372
environment
environment
"seashore.jpg" "glacier.jpg" "poppyfield.jpg"
2

BUTTON
150
48
277
81
NIL
change-environment
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

SLIDER
6
64
98
97
bug-size
bug-size
0.5
5
1
0.5
1
NIL
HORIZONTAL

BUTTON
280
48
394
81
clear environment
clear-drawing
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

SLIDER
6
106
149
139
max-mutation-step
max-mutation-step
0
100
26
1
1
NIL
HORIZONTAL

PLOT
6
199
166
319
Bugs Caught vs. Time
seconds
bugs
0.0
10.0
0.0
10.0
true
false
"" ""
PENS
"default" 1.0 0 -16777216 true "" "plotxy ticks total-caught"

MONITOR
407
317
489
362
bugs in world
count bugs
0
1
11

PLOT
7
329
278
449
Average HSB Values
time
value
0.0
10.0
0.0
255.0
true
true
"" ""
PENS
"hue" 1.0 0 -16777216 true "" "plotxy ticks mean [item 0 extract-hsb  approximate-rgb (item 0 color) (item 1 color) (item 2 color)] of bugs"
"brightness" 1.0 0 -955883 true "" "plotxy ticks mean [item 2 extract-hsb approximate-rgb (item 0 color) (item 1 color) (item 2 color)] of bugs"
"saturation" 1.0 0 -7500403 true "" "plotxy ticks mean [item 1 extract-hsb approximate-rgb (item 0 color) (item 1 color) (item 2 color)] of bugs"

@#$#@#$#@
## WHAT IS IT?

This is a model of natural/artificial selection that shows how a population hunted by a predator can develop camouflaging.  For example, in a forest with green leaves, green bugs may emerge as the predominant bug color.

When a predator uses color and shape to identify the location of prey in an environment, then the colors and patterns in the environment provide additional selective pressure on the prey.  If some prey tend to blend into the background better, they tend to survive longer and reproduce more often.  If this continues over many generations, the distribution of colors in a population may shift to become better camouflaged in the surrounding environment.

## HOW IT WORKS

You assume the role of a predator bird.  After you press GO, click on bugs as fast as you can using your mouse.  You may also keep the mouse button depressed, and move the cursor around the world.

You can watch your consumption by watching the "Bugs Caught vs. Time" plot.  As its slope decreases, you efficiency (or, in other words, your rate of bug consumption) is decreasing.  Also, the time to catch a batch of bugs is plotted in the "Time to Catch 10 Bugs" plot.

Try to consume bugs as fast as possible to remove any "deliberation" on your part as a predator.  The camouflaging effect will emerge more clearly if you aren't taking your time trying to find bugs that typically would be more difficult to find.

Bugs have 3 genes that determine their phenotype for color.  One gene is RED-PIGMENT-GENE-FREQUENCY, another is GREEN-PIGMENT-GENE-FREQUENCY, and the last is BLUE-PIGMENT-GENE-FREQUENCY.  The more frequently the gene for a pigment is coded for, the stronger that presence of color is in the overall blend of pigments that results in a single phenotype for coloration (determined by an RGB [Red-Green-Blue] calculation).

With each bug you eat, a new bug is randomly chosen to reproduce one offspring.  The offspring's gene-frequency for each of the three pigment genes may be different than the parent (as determined by the MUTATION-STEP slider).

## HOW TO USE IT

CARRYING-CAPACITY determines the size of the population on SETUP, and how many bugs are in the world at one time when GO is pressed and bugs are being eaten.

MAX-MUTATION-STEP determines how much the pigment genes can drift from their current values in each new generation.  For example, a MUTATION-STEP of 1 means that the gene frequency for any of the three pigments could go up 1, down 1, or not change at all in the offspring.

OFFSPRING-DISTANCE determines how far away (in patches) an offspring could show up from a parent.  For example, a distance of 5 means the offspring could be 0, 1, 2, 3, 4, or 5 patches away from the parent.

BUG-SIZE can be changed at any time during GO or before SETUP to modify the size of the shapes for the bugs.

SHOW-GENOTYPE? reveals the RGB (Red-Green-Blue) gene frequency values for each bug.  The values for Red can range from 0 to 100, and this also true for Green and Blue.  These numbers represent how fully expressed each pigment is (e.g. 40-100-24 would represent genetic information that expresses the red pigment at 40% its maximum value, the green pigment at 100%, and the blue pigment at 24%.

ENVIRONMENT specifies the file name to load as a background image on SETUP or on CHANGE-ENVIRONMENT.  The image file must be located in the same directory as the model.

MAKE-SINGLE-GENERATION creates one offspring from the existing bugs, without being limited by the CARRYING-CAPACITY.  This can only be used, when the model is paused and GO is not running.

TOTAL-CAUGHT keeps track of how many bugs you have caught.

BUGS IN WORLD keeps track of how many bugs are in the world & view.

The plot "BUGS CAUGHT VS TIME" give a measure of your progress and performance as a predator.

The plot of "AVERAGE HSB VALUES" shows how the average values for the hue, saturation, and brightness of the bugs changes over time.

The histograms of "CURRENT HUES" and "INITIAL HUES" shows a distribution of hues in the current and starting populations.

The histograms of "CURRENT SATURATIONS" and "INITIAL SATURATIONS" shows a distribution of saturations (of colors) in the current and starting populations.  Low values represent "grayish" colorations and high values represent "vivid" colorations.

The histograms of "CURRENT BRIGHTNESSES" and "INITIAL BRIGHTNESSES" shows a distribution of brightnesses (of colors) in the current and starting populations.  Low values represent "dark" colorations and high values represent "light" colorations.

The plot of "VECTOR DIFFERENCE IN AVERAGE GENOTYPE" shows how the average values the genotype of the population change overtime.  The plot shows the vector difference between the average value of red gene frequency, green gene frequency, and blue gene frequency for the current population as compared to the initial population.

## THINGS TO NOTICE

The first thing that you will notice is that, over time, bugs will become harder and harder to detect in the environment.  The completely random mutation process is pushed in a particular direction due to selective pressure which you unintentionally exert on the bugs.

Camouflaging emerges from the interaction of genes that encode for color, selective pressure, and small random changes (mutations) that accumulate in the offspring of survivors.

Each new offspring starts at zero size and grows to full size (specified by BUG-SIZE) after a while.  This growth in size is included to make brand new offspring harder to detect.  If newly created offspring were full sized right away, your eyes would more easily detect the "flash effect" created by sudden appearance of something new on a static background.

Sometimes two or more "near background" colors emerge as a predominant feature in a population of bugs.  An example of this is the appearance of mostly green and red bugs in the poppy field, or dark blue/black and snow blue in the glacier background.  Other times, the saturation of the bugs appears to be selected for.  An example of this is a common outcome of "shell colored" bugs on the seashore background (e.g. light yellow, light tan, and light blue bugs similar to the shells of the seashore).

Larger numbers of bugs tend to take longer to start camouflaging.

In environments that have two distinct areas (such as a ground and sky), each with their own patterns and background colors, you might see two distinct populations of different camouflaging outcomes.  Often, while hunting in one area, you will be surprised to look over at the other area (after they hadn't been paying attention to that area in a while) and notice that now there are a bunch of bugs in that background that blend in this new area very well, but whose colors are distinctly different than those that blend into the original area you were hunting in.

## THINGS TO TRY

Try this model with a user who has never seen it.  A common response from the user (within about 1 minute of interaction with the model) is "where did the bugs all go?"  If you keep playing with the model, the user might get better at finding the bugs, but if s/he keeps trying to catch bugs quickly, even an experienced user will find that the creatures will become very hard to find in certain environments.

Once you reach a point where you are having trouble finding the bugs, it is useful to either press FLASH to show where they are (and how they are camouflaged), or press CLEAR-BACKGROUND to enable you to study their color distribution and location.

## EXTENDING THE MODEL

What if you could eat a batch of bugs (e.g. 5 or 10) before they reproduced?

What if bugs reproduced sexually and recombined gene frequencies in their offspring?

What if bugs moved?

## NETLOGO FEATURES

`import-drawing` is the primitive that loads the image into the drawing, which in this case is merely a backdrop instead of something agents actually draw in.

Because this model depends on interaction with the user, we measure the passage of time in seconds.  We use `timer` to measure how much time passes each time through `go`, and we use `tick-advance` to advance the clock by that amount.  (A simpler approach would have been to not use the tick counter at all and only use `timer`, but the problem with `timer` is that it keeps advancing even when `go` isn't pressed.)

In the code for flashing the bugs, we use `ask-concurrent`.  We could have avoided it as follows, but the downside is that an extra turtle variable would have been required.

      turtles-own [old-color]
    
      ask bugs [ set old-color color ]
      repeat 3 [
        ask bugs [ set color black ]
        display
        wait 0.1
        ask turtles [ set color white ]
        display
        wait 0.1
      ]
      ask bugs [ set color old-color ]
      display


## RELATED MODELS

Bug Hunt Speeds  
Peppered Moths

## CREDITS AND REFERENCES

Inspired by this: BugHunt! Macintosh freeware: http://bcrc.bio.umass.edu/BugHunt/


## HOW TO CITE

If you mention this model in a publication, we ask that you include these citations for the model itself and for the NetLogo software:  
- Novak, M. and Wilensky, U. (2005).  NetLogo Bug Hunt Camouflage model.  http://ccl.northwestern.edu/netlogo/models/BugHuntCamouflage.  Center for Connected Learning and Computer-Based Modeling, Northwestern University, Evanston, IL.  
- Wilensky, U. (1999). NetLogo. http://ccl.northwestern.edu/netlogo/. Center for Connected Learning and Computer-Based Modeling, Northwestern University, Evanston, IL.  

## COPYRIGHT AND LICENSE

Copyright 2005 Uri Wilensky.

![CC BY-NC-SA 3.0](http://i.creativecommons.org/l/by-nc-sa/3.0/88x31.png)

This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License.  To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons, 559 Nathan Abbott Way, Stanford, California 94305, USA.

Commercial licenses are also available. To inquire about commercial licenses, please contact Uri Wilensky at uri@northwestern.edu.
@#$#@#$#@
default
true
0
Polygon -7500403 true true 150 5 40 250 150 205 260 250

airplane
true
0
Polygon -7500403 true true 150 0 135 15 120 60 120 105 15 165 15 195 120 180 135 240 105 270 120 285 150 270 180 285 210 270 165 240 180 180 285 195 285 165 180 105 180 60 165 15

arrow
true
0
Polygon -7500403 true true 150 0 0 150 105 150 105 293 195 293 195 150 300 150

box
false
0
Polygon -7500403 true true 150 285 285 225 285 75 150 135
Polygon -7500403 true true 150 135 15 75 150 15 285 75
Polygon -7500403 true true 15 75 15 225 150 285 150 135
Line -16777216 false 150 285 150 135
Line -16777216 false 150 135 15 75
Line -16777216 false 150 135 285 75

bug
true
0
Circle -7500403 true true 96 182 108
Circle -7500403 true true 110 127 80
Circle -7500403 true true 110 75 80
Line -7500403 true 150 100 80 30
Line -7500403 true 150 100 220 30

butterfly
true
0
Polygon -7500403 true true 150 165 209 199 225 225 225 255 195 270 165 255 150 240
Polygon -7500403 true true 150 165 89 198 75 225 75 255 105 270 135 255 150 240
Polygon -7500403 true true 139 148 100 105 55 90 25 90 10 105 10 135 25 180 40 195 85 194 139 163
Polygon -7500403 true true 162 150 200 105 245 90 275 90 290 105 290 135 275 180 260 195 215 195 162 165
Polygon -16777216 true false 150 255 135 225 120 150 135 120 150 105 165 120 180 150 165 225
Circle -16777216 true false 135 90 30
Line -16777216 false 150 105 195 60
Line -16777216 false 150 105 105 60

car
false
0
Polygon -7500403 true true 300 180 279 164 261 144 240 135 226 132 213 106 203 84 185 63 159 50 135 50 75 60 0 150 0 165 0 225 300 225 300 180
Circle -16777216 true false 180 180 90
Circle -16777216 true false 30 180 90
Polygon -16777216 true false 162 80 132 78 134 135 209 135 194 105 189 96 180 89
Circle -7500403 true true 47 195 58
Circle -7500403 true true 195 195 58

circle
false
0
Circle -7500403 true true 0 0 300

circle 2
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240

cow
false
0
Polygon -7500403 true true 200 193 197 249 179 249 177 196 166 187 140 189 93 191 78 179 72 211 49 209 48 181 37 149 25 120 25 89 45 72 103 84 179 75 198 76 252 64 272 81 293 103 285 121 255 121 242 118 224 167
Polygon -7500403 true true 73 210 86 251 62 249 48 208
Polygon -7500403 true true 25 114 16 195 9 204 23 213 25 200 39 123

cylinder
false
0
Circle -7500403 true true 0 0 300

dot
false
0
Circle -7500403 true true 90 90 120

face happy
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 255 90 239 62 213 47 191 67 179 90 203 109 218 150 225 192 218 210 203 227 181 251 194 236 217 212 240

face neutral
false
0
Circle -7500403 true true 8 7 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Rectangle -16777216 true false 60 195 240 225

face sad
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 168 90 184 62 210 47 232 67 244 90 220 109 205 150 198 192 205 210 220 227 242 251 229 236 206 212 183

fish
false
0
Polygon -1 true false 44 131 21 87 15 86 0 120 15 150 0 180 13 214 20 212 45 166
Polygon -1 true false 135 195 119 235 95 218 76 210 46 204 60 165
Polygon -1 true false 75 45 83 77 71 103 86 114 166 78 135 60
Polygon -7500403 true true 30 136 151 77 226 81 280 119 292 146 292 160 287 170 270 195 195 210 151 212 30 166
Circle -16777216 true false 215 106 30

flag
false
0
Rectangle -7500403 true true 60 15 75 300
Polygon -7500403 true true 90 150 270 90 90 30
Line -7500403 true 75 135 90 135
Line -7500403 true 75 45 90 45

flower
false
0
Polygon -10899396 true false 135 120 165 165 180 210 180 240 150 300 165 300 195 240 195 195 165 135
Circle -7500403 true true 85 132 38
Circle -7500403 true true 130 147 38
Circle -7500403 true true 192 85 38
Circle -7500403 true true 85 40 38
Circle -7500403 true true 177 40 38
Circle -7500403 true true 177 132 38
Circle -7500403 true true 70 85 38
Circle -7500403 true true 130 25 38
Circle -7500403 true true 96 51 108
Circle -16777216 true false 113 68 74
Polygon -10899396 true false 189 233 219 188 249 173 279 188 234 218
Polygon -10899396 true false 180 255 150 210 105 210 75 240 135 240

hawk
true
0
Polygon -7500403 true true 166 230 165 255 138 289 158 304 171 304 194 289 165 255
Polygon -16777216 true false 166 222 165 247 143 285 155 295 176 294 189 285 165 247
Polygon -7500403 true true 175 164 174 151 164 144 145 143 154 152 148 171 142 203 150 240 154 262 165 296 175 264 180 240 184 201
Polygon -7500403 true true 153 175 224 159 285 179 271 183 293 199 275 200 286 213 269 213 277 223 263 220 266 227 227 232 152 229
Circle -16777216 true false 159 145 7
Polygon -16777216 true false 144 143 150 148 154 144
Polygon -7500403 true true 168 171 94 158 35 177 51 182 27 197 45 198 34 211 51 211 43 221 57 218 54 225 93 230 168 227
Polygon -7500403 true true 165 214 164 239 142 277 154 287 175 286 188 277 164 239
Polygon -16777216 true false 171 251 164 246 143 270 155 280 176 279 189 270 161 251
Polygon -7500403 true true 165 201 164 226 149 261 154 274 175 273 181 260 164 226
Polygon -16777216 false false 145 142 154 152 149 168 92 158 33 176 49 182 25 196 45 197 33 211 52 211 42 221 57 218 52 225 93 231 147 228 150 240 150 252 149 267 143 271 145 272 141 276 145 281 137 289 150 300 177 300 194 289 184 279 189 275 185 274 188 271 179 262 181 258 176 253 181 240 181 230 228 233 267 227 261 219 277 222 267 213 286 213 275 200 292 198 271 182 285 178 226 159 175 170 174 151 164 144

house
false
0
Rectangle -7500403 true true 45 120 255 285
Rectangle -16777216 true false 120 210 180 285
Polygon -7500403 true true 15 120 150 15 285 120
Line -16777216 false 30 120 270 120

leaf
false
0
Polygon -7500403 true true 150 210 135 195 120 210 60 210 30 195 60 180 60 165 15 135 30 120 15 105 40 104 45 90 60 90 90 105 105 120 120 120 105 60 120 60 135 30 150 15 165 30 180 60 195 60 180 120 195 120 210 105 240 90 255 90 263 104 285 105 270 120 285 135 240 165 240 180 270 195 240 210 180 210 165 195
Polygon -7500403 true true 135 195 135 240 120 255 105 255 105 285 135 285 165 240 165 195

line
true
0
Line -7500403 true 150 0 150 300

line half
true
0
Line -7500403 true 150 0 150 150

moth
true
0
Polygon -7500403 true true 150 165 209 199 225 225 225 255 195 270 165 255 150 240
Polygon -7500403 true true 150 165 89 198 75 225 75 255 105 270 135 255 150 240
Polygon -7500403 true true 139 148 100 105 55 90 25 90 10 105 10 135 25 180 40 195 85 194 139 163
Polygon -7500403 true true 162 150 200 105 245 90 275 90 290 105 290 135 275 180 260 195 215 195 162 165
Polygon -7500403 true true 150 255 135 225 120 150 135 120 150 105 165 120 180 150 165 225
Circle -7500403 true true 135 90 30
Line -7500403 true 150 105 195 60
Line -7500403 true 150 105 105 60

moth-black
true
0
Polygon -16777216 true false 150 165 209 199 225 225 225 255 195 270 165 255 150 240
Polygon -16777216 true false 150 165 89 198 75 225 75 255 105 270 135 255 150 240
Polygon -16777216 true false 139 148 100 105 55 90 25 90 10 105 10 135 25 180 40 195 85 194 139 163
Polygon -16777216 true false 162 150 200 105 245 90 275 90 290 105 290 135 275 180 260 195 215 195 162 165
Polygon -16777216 true false 150 255 135 225 120 150 135 120 150 105 165 120 180 150 165 225
Circle -16777216 true false 135 90 30
Line -16777216 false 150 105 195 60
Line -16777216 false 150 105 105 60

moth-white
true
0
Polygon -1 true false 150 165 209 199 225 225 225 255 195 270 165 255 150 240
Polygon -1 true false 150 165 89 198 75 225 75 255 105 270 135 255 150 240
Polygon -1 true false 139 148 100 105 55 90 25 90 10 105 10 135 25 180 40 195 85 194 139 163
Polygon -1 true false 162 150 200 105 245 90 275 90 290 105 290 135 275 180 260 195 215 195 162 165
Polygon -1 true false 150 255 135 225 120 150 135 120 150 105 165 120 180 150 165 225
Circle -1 true false 135 90 30
Line -1 false 150 105 195 60
Line -1 false 150 105 105 60

pentagon
false
0
Polygon -7500403 true true 150 15 15 120 60 285 240 285 285 120

person
false
0
Circle -7500403 true true 110 5 80
Polygon -7500403 true true 105 90 120 195 90 285 105 300 135 300 150 225 165 300 195 300 210 285 180 195 195 90
Rectangle -7500403 true true 127 79 172 94
Polygon -7500403 true true 195 90 240 150 225 180 165 105
Polygon -7500403 true true 105 90 60 150 75 180 135 105

plant
false
0
Rectangle -7500403 true true 135 90 165 300
Polygon -7500403 true true 135 255 90 210 45 195 75 255 135 285
Polygon -7500403 true true 165 255 210 210 255 195 225 255 165 285
Polygon -7500403 true true 135 180 90 135 45 120 75 180 135 210
Polygon -7500403 true true 165 180 165 210 225 180 255 120 210 135
Polygon -7500403 true true 135 105 90 60 45 45 75 105 135 135
Polygon -7500403 true true 165 105 165 135 225 105 255 45 210 60
Polygon -7500403 true true 135 90 120 45 150 15 180 45 165 90

square
false
0
Rectangle -7500403 true true 30 30 270 270

square 2
false
0
Rectangle -7500403 true true 30 30 270 270
Rectangle -16777216 true false 60 60 240 240

star
false
0
Polygon -7500403 true true 151 1 185 108 298 108 207 175 242 282 151 216 59 282 94 175 3 108 116 108

target
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240
Circle -7500403 true true 60 60 180
Circle -16777216 true false 90 90 120
Circle -7500403 true true 120 120 60

tree
false
0
Circle -7500403 true true 118 3 94
Rectangle -6459832 true false 120 195 180 300
Circle -7500403 true true 65 21 108
Circle -7500403 true true 116 41 127
Circle -7500403 true true 45 90 120
Circle -7500403 true true 104 74 152

triangle
false
0
Polygon -7500403 true true 150 30 15 255 285 255

triangle 2
false
0
Polygon -7500403 true true 150 30 15 255 285 255
Polygon -16777216 true false 151 99 225 223 75 224

truck
false
0
Rectangle -7500403 true true 4 45 195 187
Polygon -7500403 true true 296 193 296 150 259 134 244 104 208 104 207 194
Rectangle -1 true false 195 60 195 105
Polygon -16777216 true false 238 112 252 141 219 141 218 112
Circle -16777216 true false 234 174 42
Rectangle -7500403 true true 181 185 214 194
Circle -16777216 true false 144 174 42
Circle -16777216 true false 24 174 42
Circle -7500403 false true 24 174 42
Circle -7500403 false true 144 174 42
Circle -7500403 false true 234 174 42

turtle
true
0
Polygon -10899396 true false 215 204 240 233 246 254 228 266 215 252 193 210
Polygon -10899396 true false 195 90 225 75 245 75 260 89 269 108 261 124 240 105 225 105 210 105
Polygon -10899396 true false 105 90 75 75 55 75 40 89 31 108 39 124 60 105 75 105 90 105
Polygon -10899396 true false 132 85 134 64 107 51 108 17 150 2 192 18 192 52 169 65 172 87
Polygon -10899396 true false 85 204 60 233 54 254 72 266 85 252 107 210
Polygon -7500403 true true 119 75 179 75 209 101 224 135 220 225 175 261 128 261 81 224 74 135 88 99

wheel
false
0
Circle -7500403 true true 3 3 294
Circle -16777216 true false 30 30 240
Line -7500403 true 150 285 150 15
Line -7500403 true 15 150 285 150
Circle -7500403 true true 120 120 60
Line -7500403 true 216 40 79 269
Line -7500403 true 40 84 269 221
Line -7500403 true 40 216 269 79
Line -7500403 true 84 40 221 269

x
false
0
Polygon -7500403 true true 270 75 225 30 30 225 75 270
Polygon -7500403 true true 30 75 75 30 270 225 225 270

@#$#@#$#@
NetLogo 5.0
@#$#@#$#@
setup
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
default
0.0
-0.2 0 0.0 1.0
0.0 1 1.0 0.0
0.2 0 0.0 1.0
link direction
true
0
Line -7500403 true 150 150 90 180
Line -7500403 true 150 150 210 180

@#$#@#$#@
0
@#$#@#$#@
