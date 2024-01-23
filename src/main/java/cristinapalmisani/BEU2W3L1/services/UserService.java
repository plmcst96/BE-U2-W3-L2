package cristinapalmisani.BEU2W3L1.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import cristinapalmisani.BEU2W3L1.entities.User;
import cristinapalmisani.BEU2W3L1.exception.NotFoundException;
import cristinapalmisani.BEU2W3L1.payloads.user.UserRequestDTO;
import cristinapalmisani.BEU2W3L1.repositories.UserRepositoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserRepositoryDAO userRepositoryDAO;
    @Autowired
    private Cloudinary cloudinary;



    public Page<User> getUsers(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return userRepositoryDAO.findAll(pageable);
    }



    public User findById(UUID id) throws NotFoundException {
        return userRepositoryDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(UUID id) throws NotFoundException {
        User user = userRepositoryDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        userRepositoryDAO.delete(user);
    }

    public User findByIdAndUpdate(UUID id, UserRequestDTO body) throws NotFoundException {
        User u = userRepositoryDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        u.setName(body.name());
        u.setSurname(body.surname());
        u.setEmail(body.email());
        return userRepositoryDAO.save(u);
    }

    public String uploadPicture(UUID id, MultipartFile file) throws IOException {
        User user = userRepositoryDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        user.setAvatar(url);
        userRepositoryDAO.save(user);
        return url;
    }

    public User findByEmail(String email) throws NotFoundException {
        return userRepositoryDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovata!"));
    }


}
