package eisenwave.radar.data;

import org.jetbrains.annotations.NotNull;

public class BukkitVersion implements Comparable<BukkitVersion> {
    
    private final String str;
    private final int first, second, third;
    //private final String protocol;
    
    public BukkitVersion(@NotNull String version) {
        this.str = version;
        String[] parts = version.split("-", 2);
        String[] nums = parts[0].split("\\.", 3);
        
        this.first = Integer.parseInt(nums[0]);
        this.second = Integer.parseInt(nums[1]);
        this.third = nums.length > 2? Integer.parseInt(nums[2]) : 0;
    }
    
    /**
     * Returns the first number of the version. For instance, for 1.12.2, this would return 1.
     *
     * @return the first number of the version
     */
    public int getFirst() {
        return first;
    }
    
    /**
     * Returns the second number of the version. For instance, for 1.12.2, this would return 12.
     *
     * @return the second number of the version
     */
    public int getSecond() {
        return second;
    }
    
    /**
     * Returns the third number of the version. For instance, for 1.12.2, this would return 2.
     * <p>
     * If the version doesn't have a third number, such as the version 1.12, this returns 0.
     *
     * @return the third number of the version
     */
    public int getThird() {
        return third;
    }
    
    // PREDICATES
    
    /**
     * Tests whether this version is more recent than the given version.
     *
     * @param version the version
     * @return whether this version is newer
     */
    public boolean isNewerThan(BukkitVersion version) {
        return compareTo(version) > 0;
    }
    
    /**
     * Tests whether this version is more or equally recent than the given version.
     *
     * @param version the version
     * @return whether this version is newer or equal
     */
    public boolean isNewerEqual(BukkitVersion version) {
        return compareTo(version) >= 0;
    }
    
    /**
     * Tests whether this version is less recent than the given version.
     *
     * @param version the version
     * @return whether this version is older
     */
    public boolean isOlderThan(BukkitVersion version) {
        return compareTo(version) < 0;
    }
    
    /**
     * Tests whether this version is less or equally recent than the given version.
     *
     * @param version the version
     * @return whether this version is newer or equal
     */
    public boolean isOlderEqual(BukkitVersion version) {
        return compareTo(version) <= 0;
    }
    
    // MISC
    
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    
    public boolean equals(BukkitVersion version) {
        return this.first == version.first
            && this.second == version.second
            && this.third == version.third;
    }
    
    @Override
    public String toString() {
        return str;
    }
    
    /**
     * Compares this version to another version. This comparator sorts versions from the oldest to most recent.
     *
     * @param version the version
     * @return the comparison result
     */
    @Override
    public int compareTo(@NotNull BukkitVersion version) {
        int result = this.getFirst() - version.getFirst();
        if (result != 0) return result;
        
        result = this.getSecond() - version.getSecond();
        if (result != 0) return result;
        
        return this.getThird() - version.getThird();
    }
    
}
