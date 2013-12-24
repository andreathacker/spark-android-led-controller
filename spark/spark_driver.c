
typedef struct led_data led_data;
struct led_data{
    int start = 0;
    int end = 0;
    int loop = 0;
    int speed = 0;
    int led;
};

led_data *led1 = (led_data*)malloc(sizeof(led_data));
led_data *led2 = (led_data*)malloc(sizeof(led_data));


// This routine runs only once upon reset
void setup()
{
   //serial connection for debugging
   Serial.begin(9600);   
   //Register Spark function here
   Spark.function("led", ledControl);
   
   //Assign ports to led values
   led1->led = A0;
   led2->led = A1;
   
   // Configure the pins to be outputs
   pinMode(led1->led, OUTPUT);
   pinMode(led2->led, OUTPUT);

   // Initialize both the LEDs to be OFF
   digitalWrite(led1->led, LOW);
   digitalWrite(led2->led, LOW);
}


// This routine loops forever 
void loop()
{
    int temp = 0;
    if(led1->loop){
        fadeInto(led1->led,led1->start,led1->end,led1->speed);
        temp = led1->start;
        led1->start = led1->end;
        led1->end = temp;
    }
    if(led2->loop){
        fadeInto(led2->led,led2->start,led2->end,led2->speed);
        temp = led2->start;
        led2->start = led2->end;
        led2->end = temp;
    }
}

// led(0,1),start(0-255),end(0-255),speed(0-5000),loop(0,1)
int ledControl(String command)
{
    Serial.println(command);
    //find out the pin number and convert the ascii to integer
    int pinNumber = command.charAt(1) - '0';
    //Sanity check to see if the pin numbers are within limits
    if (pinNumber < 0 || pinNumber > 1) return -1;
   
    led_data* curr;
    if (pinNumber == 0)  curr = led1;
    else curr = led2;
    
    //parse start brightness
    int front = 3;
    int back = command.indexOf(",",front);
    curr->start = command.substring(front,back).toInt();
    //parse end brightness
    front = back + 1;
    back = command.indexOf(",", front);
    curr->end = command.substring(front,back).toInt();
    //parse speed
    front = back + 1;
    back = command.indexOf(",", front);
    curr->speed = command.substring(front,back).toInt();
    //parse loop
    front = back + 1;
    back = command.indexOf(",", front);
    curr->loop = command.substring(front,back).toInt();
    //perform action based on data received
    if (curr->start == curr->end)
        analogWrite(curr->led,curr->start);
    if (!curr->loop)
        fadeInto(curr->led,curr->start,curr->end,curr->speed);
   else return -1;
   
   
   return 1;
}

void fadeInto(int led, int start, int end, int speed){
    int diff = (int) abs(end - start);
    int delayTime = speed / diff ;
    
    while (start != end){
        analogWrite(led,start);
        if (start > end)
            start--;
        else
            start++;
        delay(delayTime);
    }
    analogWrite(led,end);
}