# Gestures schema
MainActivity - Ekran Główny

    Exit => LongPress    
    InstructionActivity => DoubleTouch
    GameActivity => SwipeLeft
    ConfigActivity => SwipeRight
    
InstructionActivity - Instrukcja

    Return => LongPress
    Prev => SwipeRight
    Repeat => SwipeUp
    Dismiss => SwipeLeft
    
ConfigActivity - Konfiguracja

    Return => LongPress
    PairsNumberIncrease => SwipeUp
    PairsNumberDecrease => SwipeDown
    ?TimeLimitIncrease => DoubleTouch?
    ?TimeLimitDecrease => Touch?
    
GameActivity - Gra

    MoveUp => SwipeDown
    MoveDown => SwipeUp
    MoveRight => SwipeLeft
    MoveLeft => SwipeRight
    Reveal => DoubleTouch
    Exit => LongPress
