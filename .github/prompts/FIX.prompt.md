---
mode: agent
---
Fix the following issues:
- Fan speed should not have max rpm but only min rpm
- Fan alert should trigger if speed is below min rpm
- Values for Temperature and Fan Speed should not be simulated Revert the simulation and wire a real reader and refresh the values in the GUI.
- There should be a column for Status in the GUI tables to show if the sensor is OK or in Alert state.
- If a sensor is in Alert state, the entire row should be highlighted in red.
- Add a column to specify if the values come from simulated or real sensors
