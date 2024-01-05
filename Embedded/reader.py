import serial
import time
import os

def send_receive_uart(port, baud_rate):
    while True:
        try:
            # Open the serial port
            ser = serial.Serial(port, baud_rate, timeout=2)
                
            while True:
                if ser.in_waiting > 0:
                    # Read the incoming data and print it
                    incoming_data = ser.readline().decode('utf-8').strip()
                    print(f"Received: {incoming_data}")

        except serial.SerialException as e:
            print(f"Serial error: {e}")
            # Sleep for a while before retrying
            time.sleep(2)
            
        except Exception as e:
            print(f"Error: {e}")
            break  # Break the loop for other exceptions

        finally:
            try:
                # Close the serial port
                ser.close()
                print("Serial port closed.")
            except NameError:
                pass  # If 'ser' is not defined, skip closing

# Replace 'COM3' with the appropriate port name and adjust the baud rate
send_receive_uart(port='COM3', baud_rate=9600)