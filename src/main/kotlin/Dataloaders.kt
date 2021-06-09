import org.dataloader.DataLoader
import java.util.concurrent.CompletableFuture

object Dataloaders  {
  val personDataLoader = DataLoader<Int, Person>{
    CompletableFuture.supplyAsync {
      PersonRepository.findByIds(it)
    }
  }

  val petDataLoader = DataLoader<Int, Pet>{
    CompletableFuture.supplyAsync {
      PetsRepository.findByIds(it)
    }
  }
}

