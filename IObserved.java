interface IObserved{
  void notify();
  void appendListener(Observer obs);
  void removeListener(Observer obs);
}
