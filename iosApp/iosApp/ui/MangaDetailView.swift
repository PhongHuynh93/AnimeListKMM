//
//  MangaDetailView.swift
//  iosApp
//
//  Created by Coder on 10/18/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared
import KingfisherSwiftUI

struct MangaDetailView: View {
    @ObservedObject var detailMangaVM: DetailMangaVM
    
    init(manga: Manga) {
        self.detailMangaVM = DetailMangaVM(vm: DetailMangaViewModel(), manga: manga)
    }
    
    var body: some View {
        NavigationView {
            ScrollView {
                MyVStack(alignment: .leading) {
                    ForEach(detailMangaVM.list) { it in
                        let item = it.item
                        switch (item) {
                        case is Manga:
                            let manga = item as! Manga
                            KFImage(URL(string: manga.imageUrl))
                                .placeholder {
                                    VStack {
                                        Color.gray
                                    }
                                }
                                .resizable()
                                .aspectRatio(mangaRatio, contentMode:.fit)
                                .edgesIgnoringSafeArea(.all)
                        case is DetailMangaMoreInfo:
                            let moreInfo = item as! DetailMangaMoreInfo
                            VStack(alignment: .leading) {
                                Text(moreInfo.title).font(.largeTitle)
                                Text((item as! DetailMangaMoreInfo).description())
                            }
                        case is CharacterListUI:
                            let characterList = item as! CharacterListUI
                            VStack(alignment: .leading) {
                                Text(characterList.title)
                                ScrollView(.horizontal, content: {
                                    MyHStack(spacing: 16) {
                                        ForEach(characterList.list) { character in
                                            CharacterView(item: character.item)
                                        }.frame(width: 44, height: 44)
                                    }
                                }).padding(.bottom, 12)
                            }
                        default:
                            Text("Missing type implement")
                        }
                    }
                }
            }
            
        }
    }
    
    //    var body: some View {
    //        NavigationView {
    //            MyVStack(alignment: .leading) {
    //                detailMangaVM.list.forEach { it in
    //                    let item = it.item
    //                    switch (item) {
    //                    case is Manga:
    //                        let manga = item as! Manga
    //                        KFImage(URL(string: manga.imageUrl))
    //                            .placeholder {
    //                                VStack {
    //                                    Color.gray
    //                                }
    //                            }
    //                            .resizable()
    //                            .aspectRatio(mangaRatio, contentMode:.fit)
    //                    case is DetailMangaMoreInfo:
    //                        let moreInfo = item as! DetailMangaMoreInfo
    //                        VStack(alignment: .leading) {
    //                            Text(moreInfo.title).font(.largeTitle)
    //                            Text((item as! DetailMangaMoreInfo).description)
    //                        }
    //                    //                case is CharacterListUI:
    //                    //                    let characterList = item as! CharacterListUI
    //                    //                    VStack(alignment: .leading) {
    //                    //                        Text(characterList.title)
    //                    //                        ScrollView(.horizontal, content: {
    //                    //                            HStack(spacing: 16) {
    //                    //                                ForEach(characterList.list) { character in
    //                    //                                    CharacterView(item: character)
    //                    //                                }.frame(width: 44, height: 44)
    //                    //                            }
    //                    //                        }).padding(.bottom, 12)
    //                    //                    }
    //                    default:
    //                        Text("Missing type implement")
    //                    }
    //                }
    //            }
    //        }
    //    }
}
