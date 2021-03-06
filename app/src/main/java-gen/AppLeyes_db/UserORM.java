package AppLeyes_db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table USER_ORM.
 */
public class UserORM {

    private Long id;
    private Long user_id;
    private String nombre;
    private Integer esta_autenticado;
    private Integer Type;
    private String usuario;
    private String contrasena;
    private String token;

    public UserORM() {
    }

    public UserORM(Long id) {
        this.id = id;
    }

    public UserORM(Long id, Long user_id, String nombre, Integer esta_autenticado, Integer Type, String usuario, String contrasena, String token) {
        this.id = id;
        this.user_id = user_id;
        this.nombre = nombre;
        this.esta_autenticado = esta_autenticado;
        this.Type = Type;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEsta_autenticado() {
        return esta_autenticado;
    }

    public void setEsta_autenticado(Integer esta_autenticado) {
        this.esta_autenticado = esta_autenticado;
    }

    public Integer getType() {
        return Type;
    }

    public void setType(Integer Type) {
        this.Type = Type;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
