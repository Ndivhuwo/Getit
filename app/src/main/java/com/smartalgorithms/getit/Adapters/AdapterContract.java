package com.smartalgorithms.getit.Adapters;

/**
 * Copyright (c) 2017 Smart Algorithms (Pty) Ltd. All rights reserved
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class AdapterContract {
    public interface UIListener{
        void onTransition(Class<?> toClass);
    }
}
