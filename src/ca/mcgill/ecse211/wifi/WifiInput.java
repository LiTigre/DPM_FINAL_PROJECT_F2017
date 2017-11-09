package ca.mcgill.ecse211.wifi;

import java.util.Map;
import ca.mcgill.ecse211.WiFiClient.WifiConnection;
import ca.mcgill.ecse211.settings.SearchRegion;
import ca.mcgill.ecse211.settings.Setting;
import ca.mcgill.ecse211.settings.ShallowZone;
import ca.mcgill.ecse211.settings.StartingZone;
import lejos.hardware.Button;

/**
 * Example class using WifiConnection to communicate with a server and receive data concerning the
 * competition such as the starting corner the robot is placed in.
 * 
 * Keep in mind that this class is an **example** of how to use the WiFi code; you must use the
 * WifiConnection class yourself in your own code as appropriate. In this example, we simply show
 * how to get and process different types of data.
 * 
 * There are two variables you **MUST** set manually before trying to use this code.
 * 
 * 1. SERVER_IP: The IP address of the computer running the server application. This will be your
 * own laptop, until the beta beta demo or competition where this is the TA or professor's laptop.
 * In that case, set the IP to 192.168.2.3.
 * 
 * 2. TEAM_NUMBER: your project team number
 * 
 * Note: We System.out.println() instead of LCD printing so that full debug output (e.g. the very
 * long string containing the transmission) can be read on the screen OR a remote console such as
 * the EV3Control program via Bluetooth or WiFi. You can disable printing from the WiFi code via
 * ENABLE_DEBUG_WIFI_PRINT (below).
 * 
 * @author Michael Smith
 *
 */
public class WifiInput {

  // ** Set these as appropriate for your team and current situation **
  private static final String SERVER_IP = "192.168.2.3";
  private static final int TEAM_NUMBER = 2;

  // Enable/disable printing of debug info from the WiFi class
  private static final boolean ENABLE_DEBUG_WIFI_PRINT = true;

  @SuppressWarnings("rawtypes")
  public static void main(String[] args) {

    System.out.println("Running..");

    // Initialize WifiConnection class
    WifiConnection conn = new WifiConnection(SERVER_IP, TEAM_NUMBER, ENABLE_DEBUG_WIFI_PRINT);

    // Connect to server and get the data, catching any errors that might occur
    try {
      /*
       * getData() will connect to the server and wait until the user/TA presses the "Start" button
       * in the GUI on their laptop with the data filled in. Once it's waiting, you can kill it by
       * pressing the upper left hand corner button (back/escape) on the EV3. getData() will throw
       * exceptions if it can't connect to the server (e.g. wrong IP address, server not running on
       * laptop, not connected to WiFi router, etc.). It will also throw an exception if it connects
       * but receives corrupted data or a message from the server saying something went wrong. For
       * example, if TEAM_NUMBER is set to 1 above but the server expects teams 17 and 5, this robot
       * will receive a message saying an invalid team number was specified and getData() will throw
       * an exception letting you know.
       */
      Map data = conn.getData();

      int redTeam = ((Long) data.get("RedTeam")).intValue();
      int greenTeam = ((Long) data.get("GreenTeam")).intValue();
      
      int redStartingCorner = ((Long) data.get("RedCorner")).intValue();
      int greenStartingCorner = ((Long) data.get("GreenCorner")).intValue();
      
      int redZoneUpperRightX = ((Long) data.get("Red_UR_x")).intValue();
      int redZoneUpperRightY = ((Long) data.get("Red_UR_y")).intValue();
      int redZoneLowerLeftX = ((Long) data.get("Red_LL_x")).intValue();
      int redZoneLowerLeftY = ((Long) data.get("Red_LL_y")).intValue();
      
      int greenZoneUpperRightX = ((Long) data.get("Red_UR_x")).intValue();
      int greenZoneUpperRightY = ((Long) data.get("Red_UR_y")).intValue();
      int greenZoneLowerLeftX = ((Long) data.get("Red_LL_x")).intValue();
      int greenZoneLowerLeftY = ((Long) data.get("Red_LL_y")).intValue();
      
      int shallowZoneHorizontalLowerLeftX = ((Long) data.get("SH_LL_x")).intValue();
      int shallowZoneHorizontalLowerLeftY = ((Long) data.get("SH_LL_y")).intValue();
      int shallowZoneHorizontalUpperRightX = ((Long) data.get("SH_UR_x")).intValue();
      int shallowZoneHorizontalUpperRightY = ((Long) data.get("SH_UR_y")).intValue();
      int shallowZoneVerticalLowerLeftX = ((Long) data.get("SV_LL_x")).intValue();
      int shallowZoneVerticalLowerLeftY = ((Long) data.get("SV_LL_y")).intValue();
      int shallowZoneVerticalUpperRightX = ((Long) data.get("SV_UR_x")).intValue();
      int shallowZoneVerticalUpperRightY = ((Long) data.get("SV_UR_y")).intValue();
      
      int opponentFlag = 0;
      
      int ziplineStartX = ((Long) data.get("ZC_G_x")).intValue();
      int ziplineStartY = ((Long) data.get("ZC_G_y")).intValue();
      int ziplineEndX = ((Long) data.get("ZC_R_x")).intValue();
      int ziplineEndY = ((Long) data.get("ZC_R_y")).intValue();
      
      int searchRegionRedLowerLeftX = ((Long) data.get("SR_LL_x")).intValue();
      int searchRegionRedLowerLeftY = ((Long) data.get("SR_LL_y")).intValue();
      int searchRegionRedUpperRightX = ((Long) data.get("SR_UR_x")).intValue();
      int searchRegionRedUpperRightY = ((Long) data.get("SR_UR_y")).intValue();
      
      int searchRegionGreenLowerLeftX = ((Long) data.get("SG_LL_x")).intValue();
      int searchRegionGreenLowerLeftY = ((Long) data.get("SG_LL_y")).intValue();
      int searchRegionGreenUpperRightX = ((Long) data.get("SG_UR_x")).intValue();
      int searchRegionGreenUpperRightY = ((Long) data.get("SG_UR_y")).intValue();
      
      if (TEAM_NUMBER == redTeam) {
      	Setting.setTeamColor(Setting.TeamColor.Red);
      	Setting.setStartingCorner(redStartingCorner);
      	opponentFlag = ((Long) data.get("OR")).intValue();
      } else if (TEAM_NUMBER == greenTeam) {
      	Setting.setTeamColor(Setting.TeamColor.Green);
      	Setting.setStartingCorner(greenStartingCorner);
      	opponentFlag = ((Long) data.get("OG")).intValue();
      }
      
      Setting.setOpponentFlagColor(opponentFlag);
      Setting.setZiplineStart(ziplineStartX, ziplineStartY);
      Setting.setZiplineEnd(ziplineEndX, ziplineEndY);
      
      StartingZone.setGreenZoneLowerLeftCorner(greenZoneLowerLeftX, greenZoneLowerLeftY);
      StartingZone.setGreenZoneUpperRightCorner(greenZoneUpperRightX, greenZoneUpperRightY);
      StartingZone.setRedZoneLowerLeftCorner(redZoneLowerLeftX, redZoneLowerLeftY);
      StartingZone.setRedZoneUpperRightCorner(redZoneUpperRightX, redZoneUpperRightY);
      
      ShallowZone.setHorizontalLowerLeftCorner(shallowZoneHorizontalLowerLeftX, shallowZoneHorizontalLowerLeftY);
      ShallowZone.setHorizontalUpperRightCorner(shallowZoneHorizontalUpperRightX, shallowZoneHorizontalUpperRightY);
      ShallowZone.setVerticalLowerLeftCorner(shallowZoneVerticalLowerLeftX, shallowZoneVerticalLowerLeftY);
      ShallowZone.setVerticalUpperRightCorner(shallowZoneVerticalUpperRightX, shallowZoneVerticalUpperRightY);
      
      SearchRegion.setGreenLowerLeftCorner(searchRegionGreenLowerLeftX, searchRegionGreenLowerLeftY);
      SearchRegion.setGreenUpperRightCorner(searchRegionGreenUpperRightX, searchRegionGreenUpperRightY);
      SearchRegion.setRedLowerLeftCorner(searchRegionRedLowerLeftX, searchRegionRedLowerLeftY);
      SearchRegion.setRedUpperRightCorner(searchRegionRedUpperRightX, searchRegionRedUpperRightY);

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

    // Wait until user decides to end program
    Button.waitForAnyPress();
  }
}
