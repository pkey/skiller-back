package lt.swedbank.comparators;


import lt.swedbank.beans.entity.User;

import java.util.Comparator;

public class UserByFullNameComparator implements Comparator<User> {

    @Override
    public int compare(User o1, User o2) {
        if (o1.getName().compareTo(o2.getName()) == 0) {
            return o1.getLastName().compareTo(o2.getLastName());
        } else {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
