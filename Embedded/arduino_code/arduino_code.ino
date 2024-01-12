void setup() {
  
  Serial.begin(9600); 				    // initiate serial communication
  pinMode(10, INPUT);		// set output pin mode
  pinMode(11, INPUT);		// set output pin mode
}

void loop() {
  if ((digitalRead(10) ==1) || (digitalRead(11)==1)){
    Serial.println('0');
  }
  else{
    // send the vaue to analog input 0;
    Serial.println(analogRead(A0));
    
  }
  delay(1);
  
}