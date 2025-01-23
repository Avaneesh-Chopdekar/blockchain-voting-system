package entities;

public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String role;
    private String voterId;
    private boolean isVoterIdValid;

    public User() {
    }

    public User(Long id, String username, String passwordHash, String role, String voterId, boolean isVoterIdValid) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.voterId = voterId;
        this.isVoterIdValid = isVoterIdValid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public boolean isVoterIdValid() {
        return isVoterIdValid;
    }

    public void setVoterIdValid(boolean isVoterIdValid) {
        this.isVoterIdValid = isVoterIdValid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((passwordHash == null) ? 0 : passwordHash.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
        result = prime * result + ((voterId == null) ? 0 : voterId.hashCode());
        result = prime * result + (isVoterIdValid ? 1231 : 1237);
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
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (passwordHash == null) {
            if (other.passwordHash != null)
                return false;
        } else if (!passwordHash.equals(other.passwordHash))
            return false;
        if (role == null) {
            if (other.role != null)
                return false;
        } else if (!role.equals(other.role))
            return false;
        if (voterId == null) {
            if (other.voterId != null)
                return false;
        } else if (!voterId.equals(other.voterId))
            return false;
        return isVoterIdValid == other.isVoterIdValid;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", passwordHash=" + passwordHash + ", role=" + role
                + ", voterId=" + voterId + ", isVoterIdValid=" + isVoterIdValid + "]";
    }

}