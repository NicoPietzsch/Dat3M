procedure main()
{
  var counter: int;
  counter := 0;
  assert(counter == 0);
  call pthread_create($p3, $0.ref, thrd0, $0.ref);
  counter := 1;
  assert(counter == 1);
  call pthread_create($p3, $0.ref, thrd1, $0.ref);
  counter := 2;
  assert(counter == 2);
}

procedure thrd0()
{
  var counter: int;
  counter := 1;
  assert(counter == 1);
}

procedure thrd1()
{
  var counter: int;
  counter := 2;
  assert(counter == 1);
}