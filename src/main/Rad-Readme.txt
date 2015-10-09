RAD Info

Requirements:

 + Gson and JSON Simple JARS

 + Generic Framework - These file depend on the Rover Client classes

Notes:

 + RadMain.java contains the main class, this is placed under the ‘main’ package of the Generic Framework. Same place where MasterMain.java is located.


Updates:

 + RadServer.java - the JSON path (String path) at the top is now "3.json"

JSON:

 + JSON contains something that looks like this:

It has a power level and its state, as well as the data collected, which is a list of times in seconds and the radiation level at that second.


{
  "powerLevel": 0.0,
  "data": {
    "1432398661977": 7864.786651224616,
    "1432400404083": 9364.75620019206,
    "1432400404084": 2334.7547304122045,
    "1431811752624": 0.7942673966034984,
    "1432398661979": 771.8006953387928,
    "1432400404081": 2965.732554085342,
    "1431811751403": 0.36730335969863137,
    "1431811752013": 0.6628148819329815
  },
  "state": "RAD_OFF"
}
