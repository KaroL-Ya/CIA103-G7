package com.lookcafe;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



@Service("lookCafeService")
 public class LookCafeService {

  @Autowired
  LookCafeRepository repository;

  @Autowired
  private SessionFactory sessionFactory;

  public void addLookCafe(LookCafeVO lookCafeVO) {
   repository.save(lookCafeVO);
  }

  public void updateLookCafe(LookCafeVO lookCafeVO) {
   repository.save(lookCafeVO);
  }
  public List<LookCafeVO> getAll() {
   return repository.findAll();
  }


  public LookCafeVO getOneCafe(Integer cafeId) {
   Optional<LookCafeVO> optionalCafe = repository.findById(cafeId);   
   return optionalCafe.orElse(null);
  }


  public List<LookCafeVO> getAll(Map<String, String[]> map) {
   return HibernateUtil_CompositeQuery_cafe.getAllC(map, sessionFactory.openSession());
  }

  public void uploadImage(MultipartFile file, LookCafeVO cafe) throws IOException {

   byte[] imageBytes = file.getBytes();
   cafe.setImg(imageBytes); 

   repository.save(cafe);
  }

  public LookCafeVO getCafeById(Integer id) {
   return repository.findById(id).orElse(null);
  }
  public void updateCafe(LookCafeVO cafe) {
      
  repository.save(cafe); 
  }
  
    // 更新咖啡店信息
  @Transactional
   public void updateLookCafe1(LookCafeVO cafe) {
         
        repository.save(cafe);
      }
  
  

 }