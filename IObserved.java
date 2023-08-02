interface IObserved{
  void notify();
  void appendListener(IObserver obs);
  void removeListener(IObserver obs);
}
