package kigali.clinic.rw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.repository.OfficeRepository;

@Service 
public class OfficeService {
    

     @Autowired 
   private OfficeRepository officeRepo;

   public String saveOffice(Office office){
      officeRepo.save(office);
      return "saved successfully";
   }
   

   public String deleteOneOffice(int officeNumber){

      Optional<Office> getOneOffice = officeRepo.findByOfficeNumber(officeNumber);

      if(getOneOffice.isEmpty()){
            officeRepo.deleteById(getOneOffice.get().getId());

            return "Office with this number "+officeNumber+ " is deleted successfully";
      }else{
        return "we don't have office with that office Number "+officeNumber;
      }
      
   }



}
