package task3;

public class Education {
    @Save
    private String university;
    @Save
    private String graduation;
    @Save
    private int startYear;
    @Save
    private int endYear;
    @Save
    private Human curator;

    public Education() {
        super();
    }

    public Education(String university, String graduation, int startYear, int endYear, Human curator) {
        this.university = university;
        this.graduation = graduation;
        this.startYear = startYear;
        this.endYear = endYear;
        this.curator = curator;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getGraduation() {
        return graduation;
    }

    public void setGraduation(String graduation) {
        this.graduation = graduation;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    @Override
    public String toString() {
        String res =  "University: " + university + System.lineSeparator()
                + "Graduation: " + graduation + System.lineSeparator()
                + "Start Year: " + startYear + System.lineSeparator()
                + "End Year: " + endYear + System.lineSeparator()
                + "Curator: " + (curator == null ? "null" :
                ("{" + System.lineSeparator() + curator.toString() + System.lineSeparator() + "}"));
        return  res;
    }
}
