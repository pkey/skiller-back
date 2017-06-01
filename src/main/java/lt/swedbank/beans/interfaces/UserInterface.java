package lt.swedbank.beans.interfaces;

abstract class AbstractUser implements Comparable<AbstractUser> {
    String name;
    String lastname;

    public String getFullname()
    {
        return this.name + " " + this.lastname;
    }

    @Override
    public int compareTo(AbstractUser user)
    {
        return this.getFullname().compareTo(user.getFullname());
    }
}
