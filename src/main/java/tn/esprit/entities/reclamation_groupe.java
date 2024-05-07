package tn.esprit.entities;

public class reclamation_groupe {
    public Integer id;
    public Integer user_id_id;
    public Integer reclamation_agent_id_id;
    public String name;
    public String day_time;
    public String status;

    public reclamation_groupe() {
    }

    public reclamation_groupe(Integer id, Integer user_id_id, Integer reclamation_agent_id_id, String name, String day_time, String status) {
        this.id = id;
        this.user_id_id = user_id_id;
        this.reclamation_agent_id_id = reclamation_agent_id_id;
        this.name = name;
        this.day_time = day_time;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id_id() {
        return user_id_id;
    }

    public void setUser_id_id(Integer user_id_id) {
        this.user_id_id = user_id_id;
    }

    public Integer getReclamation_agent_id_id() {
        return reclamation_agent_id_id;
    }

    public void setReclamation_agent_id_id(Integer reclamation_agent_id_id) {
        this.reclamation_agent_id_id = reclamation_agent_id_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay_time() {
        return day_time;
    }

    public void setDay_time(String day_time) {
        this.day_time = day_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "reclamation_groupe{" + "id=" + id + ", user_id_id=" + user_id_id + ", reclamation_agent_id_id=" + reclamation_agent_id_id + ", name=" + name + ", dayTime=" + day_time + ", status=" + status + '}';
    }
}
