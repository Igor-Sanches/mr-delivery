package app.birdsoft.meurestaurante.adaptador;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorFragments extends FragmentPagerAdapter {

  private final List<Fragment> fragments = new ArrayList<>();
  private final List<String> titulos = new ArrayList<>();

  public AdaptadorFragments(FragmentManager fm){
    super(fm);
  }

  public void adicionar(Fragment fragment, String titulo){
    this.fragments.add(fragment);
    this.titulos.add(titulo);
  }

  @Override
  public @NotNull Fragment getItem(int possition){
    return this.fragments.get(possition);
  }

  @Override
  public int getCount(){
    return this.fragments.size();
  }

  @Override
  public String getPageTitle(int position){
    return this.titulos.get(position);
  }
}
