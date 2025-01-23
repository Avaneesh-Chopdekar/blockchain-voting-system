package entities;

public class Candidate {
    private Long id;
    private String name;
    private Long electionId;

    public Candidate(Long id, String name, Long electionId) {
        this.id = id;
        this.name = name;
        this.electionId = electionId;
    }

    public Candidate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((electionId == null) ? 0 : electionId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Candidate other = (Candidate) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (electionId == null) {
            if (other.electionId != null)
                return false;
        } else if (!electionId.equals(other.electionId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Candidate [id=" + id + ", name=" + name + ", electionId=" + electionId + "]";
    }
}