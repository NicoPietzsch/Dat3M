package dartagnan.program.event.rmw;

import dartagnan.program.Location;
import dartagnan.program.Register;
import dartagnan.program.event.Load;
import dartagnan.program.utils.EType;

public class RMWLoad extends Load {

    protected RMWLoad clone;

    public RMWLoad(Register reg, Location loc, String atomic) {
        super(reg, loc, atomic);
        addFilters(EType.RMW);
    }

    @Override
    public void beforeClone(){
        clone = null;
    }

    @Override
    public RMWLoad clone() {
        if(clone == null){
            clone = new RMWLoad(reg.clone(), loc.clone(), atomic);
            clone.setCondLevel(condLevel);
            clone.setHLId(getHLId());
            clone.setUnfCopy(getUnfCopy());
        }
        return clone;
    }
}
