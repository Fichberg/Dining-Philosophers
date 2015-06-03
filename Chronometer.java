public class Chronometer
{
    private long begin;
    private long end;
    private long new_minute;
    private long new_hour;
    
    public Chronometer()
    {
        begin = System.currentTimeMillis();
        new_hour = 0;
        new_minute = 0;
    }

    public String get_time() 
    {
        long h = 0, m = 0, s = 0;
        double time = define_time();

        m = (long) (time / 60000.0);
        if(m == 1) {
            new_minute++;
            if(new_minute == 60)
            {
                new_hour++;
                new_minute = 0;
            }
            begin = System.currentTimeMillis();
            time = define_time();
            return ("("+new_hour+"h"+new_minute+"m"+0+"s): ");
        }

        s = (long) (time / 1000.0);

        return ("("+new_hour+"h"+new_minute+"m"+s+"s): ");
    }

    private long define_time() {
        return ((end = System.currentTimeMillis()) - begin);
    }
}